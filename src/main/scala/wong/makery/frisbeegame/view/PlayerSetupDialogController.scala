package wong.makery.frisbeegame.view

import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import wong.makery.frisbeegame.MainApp

import wong.makery.frisbeegame.util.frisbeeGameDB

@sfxml
class PlayerSetupDialogController(
                                   private val usernameField: TextField,
                                   private val okButton: Button
                                 ) {

  private var dialogStage: Option[Stage] = None
  var okClicked: Boolean = false

  def setDialogStage(stage: Stage): Unit = {
    dialogStage = Some(stage)
  }

  def handleOk(): Unit = {
    if (isInputValid()) {
      val username = usernameField.text.value.trim
      MainApp.currentUsername = username // Set the username in MainApp

      // Check if the user already exists in the database
      frisbeeGameDB.getPlayerByUsername(username) match {
        case Some(existingUser) =>
          println(s"Username '$existingUser' already exists. Proceeding with existing user.")
        case None =>
          // If user does not exist, create a new player record
          frisbeeGameDB.createPlayer(username)
          println(s"New username '$username' created and added to the database.")
      }

      okClicked = true
      dialogStage.foreach(_.close())
    }
  }

  private def isInputValid(): Boolean = {
    var warningMessage = ""

    if (usernameField.text.value == null || usernameField.text.value.trim.isEmpty) {
      warningMessage += "No valid username!\n"
    }

    if (warningMessage.isEmpty) {
      true
    } else {
      // Show the error message.
      val alert = new Alert(AlertType.Warning) {
        initOwner(dialogStage.orNull)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = warningMessage
      }

      alert.showAndWait()
      false
    }
  }
}

