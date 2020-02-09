package sux.client.debug.layers

import sux.client.debug.Timer
import sux.client.rendering.drawing.Sprite
import sux.client.rendering.layers.RenderLayer
import sux.client.rendering.{DrawInfo, Sprite}
import sux.common.math.Vec2F

class DebugSpritesRenderLayer extends RenderLayer {
  val timerSprites = new Timer("Debug Sprites")
  val mySprite = new Sprite(128, 128)

  override def draw(drawInfo: DrawInfo): Unit = {
    timerSprites.markStart()

    Range(0, 1500)
          .foreach(_ => Sprite.drawSprite(drawInfo, mySprite, Vec2F(math.random().toFloat * 100f, math.random().toFloat * 100f), math.random().toFloat * math.Pi.toFloat * 2, 2f))

    timerSprites.markEnd()
  }
}
