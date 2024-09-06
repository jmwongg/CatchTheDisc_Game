package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.{KeyCode, KeyEvent}

abstract class Dog(gameWidth: Double, gameHeight: Double) {
  var imageView: ImageView = _
  var lives: Int = 5 // Initialize with 5 lives

  protected def setupImageView(imagePath: String): Unit = {
    val image = new Image(getClass.getResourceAsStream(imagePath))
    imageView = new ImageView(image)

    // Scale the image to be 1/8th of the game height
    val scaleFactor = gameHeight / 8 / image.height.value
    imageView.fitHeight = gameHeight / 8
    imageView.fitWidth = image.width.value * scaleFactor
    imageView.preserveRatio = true

    // Position the dog at the bottom center of the screen
    imageView.x = (gameWidth - imageView.fitWidth.value)
    imageView.y = gameHeight - imageView.fitHeight.value - 150 // 150 pixels from the bottom
  }

  def moveRight(): Unit = {
    if (imageView.translateX.value + imageView.x.value + imageView.fitWidth.value + 20 <= gameWidth) {
      imageView.translateX.value += 20
      imageView.scaleX = -1 // Ensure the dog faces right
    }
  }

  def moveLeft(): Unit = {
    if (imageView.translateX.value + imageView.x.value - 20 >= 0) {
      imageView.translateX.value -= 20
      imageView.scaleX = 1 // Flip the dog to face left
    }
  }

  def moveDown(): Unit = {
    if (imageView.translateY.value + imageView.y.value + imageView.fitHeight.value + 60 <= gameHeight) {
      imageView.translateY.value += 20
    }
  }

  def moveUp(): Unit = {
    if (imageView.translateY.value + imageView.y.value - 100 >= 0) {
      imageView.translateY.value -= 20
    }
  }

  def decreaseLives(): Unit = {
    lives -= 1
    if (lives <= 0) {
      lives = 0
      println("Game Over!")
    }
  }

  def handleKeyPress(event: KeyEvent): Unit = {
    event.code match {
      case KeyCode.Right => moveRight()
      case KeyCode.Left => moveLeft()
      case KeyCode.Up => moveUp()
      case KeyCode.Down => moveDown()
      case _ => // Do nothing for other keys
    }
  }
}
