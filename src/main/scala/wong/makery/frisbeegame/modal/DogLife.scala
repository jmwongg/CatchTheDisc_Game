package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}


class DogLife(gameWidth: Double, gameHeight: Double) {
  private val lifeImage = new Image(getClass.getResourceAsStream("../images/dog_life.png"))
  private val lifeImageView = new ImageView(lifeImage)

  lifeImageView.fitHeight = 70
  lifeImageView.fitWidth = 40
  lifeImageView.preserveRatio = true

  def getImageView: ImageView = lifeImageView

  def updatePosition(index: Int, xOffset: Double, yOffset: Double): Unit = {
    lifeImageView.x = xOffset + (index * (lifeImageView.fitWidth.value + 5)) // Space between life icons
    lifeImageView.y = yOffset
  }

  def setSize(width: Double, height: Double): Unit = {
    lifeImageView.fitWidth = width
    lifeImageView.fitHeight = height
  }


}
