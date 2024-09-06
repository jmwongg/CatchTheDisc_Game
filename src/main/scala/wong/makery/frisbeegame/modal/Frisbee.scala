package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}

class Frisbee(gameWidth: Double, gameHeight: Double) extends Item(gameWidth, gameHeight) {
  protected val image = new Image(getClass.getResourceAsStream("../images/orange_disc.png"))
  imageView = new ImageView(image)

  setSize(180, 180)

  reset()
}

