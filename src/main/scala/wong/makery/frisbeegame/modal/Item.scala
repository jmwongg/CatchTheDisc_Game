package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}
import scala.util.Random

abstract class Item(gameWidth: Double, gameHeight: Double) {
  var imageView: ImageView = _
  protected val image: Image
  protected var velocityX: Double = _
  protected var velocityY: Double = 0.0

  def reset(): Unit = {
    imageView.x = -image.width.value

    // Set y-coordinate to be within the specified range
    imageView.y = {
      val minY = 200
      val maxY = 800
      // Ensure y is within the bounds and greater than minY
      Random.nextDouble() * Math.max(0, maxY - minY) + minY
    }

    println(s"Item reset: x=${imageView.x.value}, y=${imageView.y.value}")

    val speed = 5 + Random.nextDouble() * 2
    velocityX = speed
    println(s"Item velocity: vX=$velocityX")
  }
  def move(): Boolean = {
    imageView.x = imageView.x.value + velocityX
    val stillInGame = imageView.x.value < gameWidth
    println(s"Item moved: x=${imageView.x.value}, y=${imageView.y.value}, still in game: $stillInGame")
    stillInGame
  }

  // Method to set the size of the ImageView
  def setSize(width: Double, height: Double): Unit = {
    imageView.fitWidth = width
    imageView.fitHeight = height
  }
}
