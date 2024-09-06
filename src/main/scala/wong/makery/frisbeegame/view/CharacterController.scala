package wong.makery.frisbeegame.view

import scalafxml.core.macros.sfxml
import wong.makery.frisbeegame.MainApp
import wong.makery.frisbeegame.modal._

@sfxml
class CharacterController{

  def selectChihuahua(): Unit = {
    print("Selected Chihuahua as character\n")
    MainApp.showGameScene((width, height) => new Chihuahua(width, height))
  }

  def selectMaltese(): Unit = {
    print("Selected Maltese as character\n")
    MainApp.showGameScene((width, height) => new Maltese(width, height))
  }

  def selectLabrador(): Unit = {
    print("Selected Labrador as character\n")
    MainApp.showGameScene((width, height) => new Labrador(width, height))
  }

  def selectBeagle(): Unit = {
    print("Selected Beagle as character\n")
    MainApp.showGameScene((width, height) => new Beagle(width, height))
  }

  def selectDachshund(): Unit = {
    print("Selected Dachshund as character\n")
    MainApp.showGameScene((width, height) => new Dachshund(width, height))
  }
}