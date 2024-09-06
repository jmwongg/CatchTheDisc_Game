package wong.makery.frisbeegame.view

import scalafx.stage.Stage
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import javafx.event.ActionEvent

@sfxml
class GameOverController(
                          restartButton: Button,
                          exitButton: Button
                        ) {

  private var dialogStage: Stage = _
  var restartClicked: Boolean = false

  def setDialogStage(stage: Stage): Unit = {
    dialogStage = stage
  }

  def handleRestart(event: ActionEvent): Unit = {
    restartClicked = true
    dialogStage.close()
  }

  def handleQuit(event: ActionEvent): Unit = {
    restartClicked = false
    dialogStage.close()
  }
}

