package wong.makery.frisbeegame.view

import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import wong.makery.frisbeegame.MainApp

@sfxml
class InstructionController(private val startGameButton: Button) {
  def getStarted(): Unit = {
    print("Start button clicked in instruction \n")
    MainApp.showCharacterSelection()
  }
}
