package wong.makery.frisbeegame.modal

import scalafx.scene.image.{Image, ImageView}

class Bone(gameWidth: Double, gameHeight: Double) extends Item(gameWidth, gameHeight) {
  protected val image = new Image(getClass.getResourceAsStream("../images/bone.png"))
  imageView = new ImageView(image)

  setSize(80, 80)

  reset()
}
