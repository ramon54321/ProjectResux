package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.Mapping.worldSpaceToCanvasSpace
import sux.client.rendering.{FrameInfo, Sprite}
import sux.common.math.Vec2F
import sux.common.math.Rect

object Sprite {
  def drawSprite(frameInfo: FrameInfo, sprite: Sprite, worldCenter: Vec2F, rotation: Float, scale: Float): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldCenter)) return
    val spriteScale = frameInfo.camera.scale * scale
    val canvasCenter = worldSpaceToCanvasSpace(frameInfo, worldCenter)
    frameInfo.context.translate(canvasCenter.x, canvasCenter.y)
    frameInfo.context.rotate(-rotation)
    frameInfo.context.drawImage(sprite.canvas, 0, 0, sprite.width, sprite.height, -sprite.height/2 * spriteScale, -sprite.width/2 * spriteScale, sprite.width * spriteScale, sprite.height * spriteScale)
    frameInfo.context.rotate(rotation)
    frameInfo.context.translate(-canvasCenter.x, -canvasCenter.y)
    RenderStatistics.drawSpriteCount += 1
  }
}
