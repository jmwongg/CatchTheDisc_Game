package wong.makery.frisbeegame.modal

import scalafx.beans.property.{IntegerProperty, StringProperty}
import wong.makery.frisbeegame.util.frisbeeGameDB

import scala.util.Try

class Player(val userName: String) {
  val username = new StringProperty(userName)
  val score = IntegerProperty(0)

  def saveScore(roundNumber: Int): Try[Unit] = {
    Try {
      frisbeeGameDB.insertScore(userName, score.value, roundNumber)
    }
  }

  def getAllScores: List[(Int, Int)] = {
    frisbeeGameDB.getPlayerScores(userName)
  }
}

object Player {
  def apply(username: String): Player = new Player(username)

  def createOrGetPlayer(username: String): Player = {
    frisbeeGameDB.getPlayerByUsername(username) match {
      case Some(_) => new Player(username)
      case None =>
        frisbeeGameDB.createPlayer(username)
        new Player(username)
    }
  }
}