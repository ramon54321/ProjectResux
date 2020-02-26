package sux.client.debug.layers

import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.Text.FontStyle
import sux.client.rendering.drawing.{Circle, Text}
import sux.client.rendering.layers.RenderLayer
import sux.common.math.{Vec2D, Vec2F}

class DebugSpacialRenderLayer extends RenderLayer {

  private val stylePoints = "rgba(230, 96, 67, 0.8)"
  private val styleFont = "rgba(255, 255, 255, 0.8)"
  private val pointCanvasRadius = 4.0

  override def draw(frameInfo: FrameInfo): Unit = {
    frameInfo.context.fillStyle = stylePoints

    Circle.drawCircleFill(frameInfo, frameInfo.screenWorldRect.topLeft, pointCanvasRadius * 2, ignoreBounds = true)
    Circle.drawCircleFill(frameInfo, frameInfo.screenWorldRect.bottomRight, pointCanvasRadius * 2, ignoreBounds = true)

    Circle.drawCircleFill(frameInfo, Vec2F(0f, 0f), pointCanvasRadius)
    Circle.drawCircleFill(frameInfo, Vec2F(100.0f, 0.0f), pointCanvasRadius)
    Circle.drawCircleFill(frameInfo, Vec2F(1000.0f, 0.0f), pointCanvasRadius)

    frameInfo.context.fillStyle = styleFont
    Text.setFont(frameInfo, FontStyle.STATIC_TINY)
    Text.drawText(frameInfo, Vec2F(0f, 0f), Vec2D(10, 10), "Origin")
    Text.drawText(frameInfo, Vec2F(100f, 0f), Vec2D(10, 10), "100m")
    Text.drawText(frameInfo, Vec2F(1000f, 0f), Vec2D(10, 10), "1,000m")
  }
}
