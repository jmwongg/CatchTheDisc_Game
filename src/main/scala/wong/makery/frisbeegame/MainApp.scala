package wong.makery.frisbeegame

import javafx.{scene => jfxs}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.geometry.Rectangle2D
import scalafx.scene.input.KeyEvent
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.stage.{Modality, Screen, Stage}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import wong.makery.frisbeegame.view.{ GameController, GameOverController, LeaderboardController, PlayerSetupDialogController}
import wong.makery.frisbeegame.modal.Dog
import wong.makery.frisbeegame.util.frisbeeGameDB

object MainApp extends JFXApp {

  private var bgmPlayer: MediaPlayer = _

  def initializeBGM(): Unit = {
    val bgmPath = getClass.getResource("audio/bgm.mp3").toExternalForm
    val media = new Media(bgmPath)
    bgmPlayer = new MediaPlayer(media) {
      cycleCount = MediaPlayer.Indefinite
    }
  }

  def playBGM(): Unit = {
    bgmPlayer.play()
  }

  def stopBGM(): Unit = {
    bgmPlayer.stop()
  }

  initializeBGM()
  playBGM()
  frisbeeGameDB.initializeTable()
  var currentDog: Option[Dog] = None // Define currentDog here
  var currentUsername: String = "" // Define currentUsername here
  var currentRound: Int = 1

  // transform path of RootLayout.fxml to URI for resource location.
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  // initialize the loader object.
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from fxml file.
  loader.load();
  // retrieve the root component BorderPane from the FXML
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  // initialize stage

  // Get the primary screen
  val primaryScreen: Screen = Screen.primary
  val screenBounds: Rectangle2D = primaryScreen.bounds

  // Define game dimensions based on screen size
  val gameWidth = screenBounds.width
  val gameHeight = screenBounds.height
  println("Game Width: " + gameWidth)
  println("Game Height: " + gameHeight)

  stage = new PrimaryStage {
    title = "Catch The Disc"
    scene = new Scene {
      stylesheets += getClass.getResource("view/Welcome.css").toString
      root = roots
    }
  }


  def showWelcome() = {
    val resource = getClass.getResource("view/Welcome.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane] //this root is type of javafx anchor pane
    this.roots.setCenter(roots)
  }

  def showInstruction(): Unit = {
    val resource = getClass.getResource("view/HowToPlay.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val instructionRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(instructionRoot)
  }

  def showCharacterSelection(): Unit = {
    if (showUsernameDialog()) {
      val resource = getClass.getResource("view/Character.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load()
      val characterRoot = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.setCenter(characterRoot)
    } else {
      // If the user cancels the username dialog, go back to the instruction screen
      showInstruction()
    }
  }

  def showUsernameDialog(): Boolean = {
    val resource = getClass.getResource("view/PlayerSetupDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val dialogRoot = loader.getRoot[jfxs.layout.AnchorPane]

    val dialogStage = new Stage {
      title = "Enter Username"
      initModality(Modality.WindowModal)
      initOwner(stage)
      scene = new Scene(dialogRoot)
    }

    val controller = loader.getController[PlayerSetupDialogController#Controller]
    controller.setDialogStage(dialogStage)

    dialogStage.showAndWait()
    controller.okClicked
  }

  def showGameScene(dogCreator: (Double, Double) => Dog): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val gameRoot = loader.getRoot[jfxs.layout.Pane]
    val gameController = loader.getController[GameController#Controller]
    //this.roots.setCenter(gameRoot)
    val gameDog = dogCreator(gameWidth, gameHeight)
    gameController.setDog(gameDog)
    gameController.initialize()
    gameController.setRoundNumber(currentRound) // Set the current round number

    // Update the scene with the new gameRoot
  stage.scene = new Scene(gameRoot) {
    onKeyPressed = (event: KeyEvent) => {
      gameController.handleKeyPress(event)
    }
  }
  stage.show()
    stage.setFullScreen(true)
  stage.requestFocus()
  println("Game scene shown")
  }

  def showGameOverDialog(): Boolean = {
    val resource = getClass.getResource("view/GameOverDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val gameOverRoot = loader.getRoot[jfxs.layout.AnchorPane]
    val dialogStage = new Stage {
      title = "Game Over"
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = gameOverRoot
      }
    }
    val controller = loader.getController[GameOverController#Controller]
    controller.setDialogStage(dialogStage)
    dialogStage.showAndWait()
    controller.restartClicked
  }

  def showLeaderboardDialog(): Unit = {
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val leaderboardRoot = loader.getRoot[jfxs.layout.AnchorPane]

    // Get the top 5 players from the database
    val topPlayers = frisbeeGameDB.getTop5Players()

    val controller = loader.getController[LeaderboardController#Controller]
    controller.setLeaderboard(topPlayers)

    val dialogStage = new Stage {
      title = "Leaderboard"
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = leaderboardRoot
      }
    }
    dialogStage.showAndWait()
  }

  showWelcome()

  }


