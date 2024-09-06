package wong.makery.frisbeegame.util

import scalikejdbc._
import wong.makery.frisbeegame.modal.Player

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = """jdbc:derby:C:\Users\Wong Jia Mien\Documents\GitHub\finalprojectv2-jmwongg\frisbeeGameDB;create=true;"""

  // Initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")

  // Ad-hoc session provider
  implicit val session: AutoSession.type = AutoSession
}

object frisbeeGameDB extends Database {
  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      // Check if player table exists
      if (!tableExists("player")) {
        sql"""
          CREATE TABLE player (
            id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
            userName VARCHAR(64) UNIQUE,
            PRIMARY KEY (id)
          )
        """.execute.apply()
      }

      // Check if rounds table exists
      if (!tableExists("rounds")) {
        sql"""
          CREATE TABLE rounds (
            id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
            playerId INT,
            score INT,
            roundNumber INT,
            FOREIGN KEY (playerId) REFERENCES player(id),
            PRIMARY KEY (id)
          )
        """.execute.apply()
      }
    }
  }

  private def tableExists(tableName: String)(implicit session: DBSession): Boolean = {
    sql"""
      SELECT COUNT(*) FROM SYS.SYSTABLES
      WHERE TABLENAME = ${tableName.toUpperCase}
    """.map(_.int(1)).single.apply().exists(_ > 0)
  }
  def getPlayerByUsername(username: String): Option[String] = {
    DB readOnly { implicit session =>
      sql"""
        SELECT userName FROM player WHERE userName = $username
      """.map(rs => rs.string("userName")).single.apply()
    }
  }

  def createPlayer(username: String): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        INSERT INTO player (userName) VALUES ($username)
      """.update.apply()
    }
  }

  def insertScore(username: String, score: Int, roundNumber: Int): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        INSERT INTO rounds (playerId, score, roundNumber)
        SELECT id, $score, $roundNumber FROM player WHERE userName = $username
      """.update.apply()
    }
  }

  def getPlayerScores(username: String): List[(Int, Int)] = {
    DB readOnly { implicit session =>
      sql"""
        SELECT r.roundNumber, r.score
        FROM rounds r
        JOIN player p ON r.playerId = p.id
        WHERE p.userName = $username
        ORDER BY r.roundNumber
      """.map(rs => (rs.int("roundNumber"), rs.int("score"))).list.apply()
    }
  }

  def getTop5Players(): List[PlayerScore] = {
    DB readOnly { implicit session =>
      sql"""
        SELECT p.userName, MAX(r.score) as maxScore
        FROM player p
        JOIN rounds r ON p.id = r.playerId
        GROUP BY p.userName
        ORDER BY maxScore DESC
        FETCH FIRST 5 ROWS ONLY
      """.map(rs => PlayerScore(rs.string("userName"), rs.int("maxScore"))).list.apply()
    }
  }

  case class PlayerScore(username: String, score: Int)


}