package wong.makery.frisbeegame.view

import scalafx.Includes.handle
import scalafx.application.Platform
import scalafx.scene.control.Button
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import wong.makery.frisbeegame.MainApp

@sfxml
class WelcomeController(private val startGame: Button) {
  def getStart(): Unit = {
    print("Start button clicked in welcome \n")
    MainApp.showInstruction()
  }

  def getLeaderboard(): Unit = {
    print("Leaderboard button clicked in welcome \n")
    MainApp.showLeaderboardDialog()
  }

  def quitGame(): Unit = {
    print("Exit game")
    Platform.exit()
  }
}
