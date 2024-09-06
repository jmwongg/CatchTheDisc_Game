package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}

class Bomb(gameWidth: Double, gameHeight: Double) extends Item(gameWidth, gameHeight) {
  protected val image = new Image(getClass.getResourceAsStream("../images/bomb.png"))
  imageView = new ImageView(image)

  setSize(120, 120)

  reset()
}
