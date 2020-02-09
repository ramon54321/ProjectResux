package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.Mapping.worldSpaceToCanvasSpace
import sux.client.rendering.{DrawInfo, Sprite}
import sux.common.math.Vec2F
import sux.common.math.Rect

object Sprite {
  def drawSprite(drawInfo: DrawInfo, sprite: Sprite, worldCenter: Vec2F, rotation: Float, scale: Float): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldCenter)) return
    val spriteScale = drawInfo.camera.scale * scale
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    drawInfo.context.translate(canvasCenter.x, canvasCenter.y)
    drawInfo.context.rotate(-rotation)
    drawInfo.context.drawImage(sprite.canvas, 0, 0, sprite.width, sprite.height, -sprite.height/2 * spriteScale, -sprite.width/2 * spriteScale, sprite.width * spriteScale, sprite.height * spriteScale)
    drawInfo.context.rotate(rotation)
    drawInfo.context.translate(-canvasCenter.x, -canvasCenter.y)
    RenderStatistics.drawSpriteCount += 1
  }
}
