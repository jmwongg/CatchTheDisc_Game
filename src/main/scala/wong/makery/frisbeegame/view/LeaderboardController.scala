package wong.makery.frisbeegame.view

import scalafx.application.Platform
import scalafxml.core.macros.sfxml
import scalafx.scene.control.TextField
import scalafx.scene.layout.AnchorPane
import wong.makery.frisbeegame.util.frisbeeGameDB.PlayerScore

@sfxml
class LeaderboardController(
                             first_username: TextField,
                             second_username: TextField,
                             third_username: TextField,
                             fourth_username: TextField,
                             fifth_username: TextField,
                             first_score: TextField,
                             second_score: TextField,
                             third_score: TextField,
                             fourth_score: TextField,
                             fifth_score: TextField
                           ) {

  def setLeaderboard(players: List[PlayerScore]): Unit = {
    // Assume players are sorted by score in descending order
    val fields = List(
      (first_username, first_score),
      (second_username, second_score),
      (third_username, third_score),
      (fourth_username, fourth_score),
      (fifth_username, fifth_score)
    )

    players.zip(fields).foreach { case (player, (usernameField, scoreField)) =>
      usernameField.text = player.username
      scoreField.text = player.score.toString
    }

    // For any remaining fields that don't have a corresponding player, clear them
    if (players.size < fields.size) {
      fields.drop(players.size).foreach { case (usernameField, scoreField) =>
        usernameField.clear()
        scoreField.clear()
      }
    }
  }
}
