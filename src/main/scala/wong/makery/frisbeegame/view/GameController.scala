package wong.makery.frisbeegame.view

import scalafx.Includes._
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Pane
import scalafx.scene.input.KeyEvent
import scalafx.animation.AnimationTimer
import scalafx.application.Platform
import scalafxml.core.macros.sfxml
import wong.makery.frisbeegame.MainApp
import wong.makery.frisbeegame.modal._
import wong.makery.frisbeegame.modal.Player
import wong.makery.frisbeegame.MainApp.{currentUsername, gameHeight, gameWidth}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Random, Success}

@sfxml
class GameController(
                      gamePane: Pane,
                      restartButton: Button,
                      exitBtn: Button,
                      scoreLabel: Label
                    ) {

  private var currentDog: Option[Dog] = None
  private val items = ListBuffer[Item]()
  private var gameLoop: AnimationTimer = _
  private var itemSpawnTimer = 0L
  private var score: Int = 0 // Initialize the score
  private var gameOver: Boolean = false
  // Declare livesDisplay as a mutable list to hold DogLife instances
  private val livesDisplay = ListBuffer[DogLife]()
  private var roundNumber: Int = 1

  // Add a method to set the round number
  def setRoundNumber(round: Int): Unit = {
    roundNumber = round
  }

  def initialize(): Unit = {
    println("GameController initialized")
    gamePane.requestFocus()
    startGameLoop()
  }

  def setDog(dog: Dog): Unit = {
    currentDog = Some(dog)
    gamePane.children.add(dog.imageView)
    gamePane.requestFocus()
    println(s"Dog set: ${dog.getClass.getSimpleName}")
    println(s"GamePane width: ${gamePane.width.value}, height: ${gamePane.height.value}")

    // Initialize the lives display
    initializeLives()
  }


  def handleKeyPress(event: KeyEvent): Unit = {
    currentDog.foreach(_.handleKeyPress(event))
  }

  private def startGameLoop(): Unit = {
    println("Starting game loop")
    gameLoop = AnimationTimer { now =>
      moveItems()
      spawnItem(now)
      checkCollisions()
    }
    gameLoop.start()
  }

  private def moveItems(): Unit = {
    items.foreach { item =>
      if (!item.move()) {
        // Remove item if it goes out of bounds
        gamePane.children.remove(item.imageView)
        items -= item
        println("Item removed from game")
      }
    }
  }

  private def spawnItem(now: Long): Unit = {
    if (now - itemSpawnTimer > 2000000000L) {
      val item = Random.nextInt(3) match {
        case 0 => new Frisbee(gameWidth, gameHeight)
        case 1 => new Bomb(gameWidth, gameHeight)
        case 2 => new Bone(gameWidth, gameHeight)
      }
      items += item
      gamePane.children.add(item.imageView)
      println(s"Item added: x=${item.imageView.x.value}, y=${item.imageView.y.value}")
      itemSpawnTimer = now
    }
  }

  private def checkCollisions(): Unit = {
    currentDog.foreach { dog =>
      items.foreach { item =>
        if (dog.imageView.boundsInParent.value.intersects(item.imageView.boundsInParent.value)) {
          item match {
            case bomb: Bomb =>
              println("Bomb caught!")
              dog.decreaseLives()
              if (dog.lives <= 0) {
                handleGameOver() // Call game over if lives are 0
              }
            case frisbee: Frisbee =>
              println("Frisbee caught!")
              updateScore(100)
            case bone: Bone =>
              println("Bone caught!")
              updateScore(50)
          }
          // Remove item from game
          gamePane.children.remove(item.imageView)
          items -= item
        }
      }
      // Update lives display
      updateLivesDisplay(dog.lives)
    }
  }

  private def initializeLives(): Unit = {
    // Clear any existing life icons
    livesDisplay.foreach { life =>
      gamePane.children.remove(life.getImageView)
    }
    livesDisplay.clear()

    // Add initial lives icons
    currentDog.foreach { dog =>
      for (i <- 0 until dog.lives) {
        val life = new DogLife(gameWidth, gameHeight)
        livesDisplay += life
        gamePane.children.add(life.getImageView)
        life.updatePosition(i, 1300, 10) // Position the life icons
      }
    }
  }


  private def updateLivesDisplay(lives: Int): Unit = {
    // Remove old life icons
    livesDisplay.foreach { life =>
      gamePane.children.remove(life.getImageView)
    }
    livesDisplay.clear()

    // Add new life icons
    for (i <- 0 until lives) {
      val life = new DogLife(gameWidth, gameHeight)
      livesDisplay += life
      gamePane.children.add(life.getImageView)
      life.updatePosition(i, 1300, 10) // Position the life icons
    }
  }

  private def updateScore(points: Int): Unit = {
    if (!gameOver) {
      score += points
      scoreLabel.text = s"Score: $score" // Update the score label
    }
  }

  private def resetGameState(): Unit = {
    // Stop the current game loop
    if (gameLoop != null) {
      gameLoop.stop()
    }
    // Remove existing items from the gamePane
    items.foreach { item =>
      gamePane.children.remove(item.imageView)
    }
    items.clear() // Clear the item list

    // Reset the score
    score = 0
    updateScore(score)

    // Reset the dog's lives
    currentDog.foreach { dog =>
      dog.lives = 5
    }

    gameOver = false // Reset game over flag
  }

  private def handleGameOver(): Unit = {
    gameOver = true
    // Save the score for the current player in the current round
    val player = Player(currentUsername)
    player.score.value = score
    player.saveScore(roundNumber) match {
      case Success(_) => println(s"Score: ${score} saved successfully for round $roundNumber")
      case Failure(exception) => println(s"Failed to save score: ${exception.getMessage}")
    }
    resetGameState() // Reset game state but not the lives display yet
    gamePane.requestFocus()
    Platform.runLater {
      val restart = MainApp.showGameOverDialog()
      if (restart) {
        roundNumber += 1 // Increment the round number only if the player decides to restart
        handleRestart() // Restart the game if the user chooses to restart
      } else {
        MainApp.showLeaderboardDialog()
        handleQuit() // Exit if the user chooses to quit
      }
    }
  }


  def handleRestart(): Unit = {
    println("Restart button clicked")
    // Reset game state and lives display
    resetGameState()
    initializeLives() // Reinitialize the lives display based on updated dog's lives
    // Restart the game loop
    startGameLoop()
    gamePane.requestFocus()
  }

  def handleQuit(): Unit = {
    println("Quit button clicked")
    gameLoop.stop()
    Platform.exit()
  }
}
