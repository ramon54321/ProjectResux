package sux.client.debug.layers

import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.Text.FontStyle
import sux.client.rendering.drawing.{Circle, Text}
import sux.client.rendering.layers.RenderLayer
import sux.common.math.{Vec2D, Vec2F}

class DebugSpacialRenderLayer extends RenderLayer {

  private val stylePoints = "rgba(230, 96, 67, 0.8)"
  private val styleFont = "rgba(255, 255, 255, 0.8)"
  private val pointCanvasRadius = 4.0

  override def draw(drawInfo: DrawInfo): Unit = {
    drawInfo.context.fillStyle = stylePoints

    Circle.drawCircleFill(drawInfo, drawInfo.screenWorldRect.topLeft, pointCanvasRadius * 2, ignoreBounds = true)
    Circle.drawCircleFill(drawInfo, drawInfo.screenWorldRect.bottomRight, pointCanvasRadius * 2, ignoreBounds = true)

    Circle.drawCircleFill(drawInfo, Vec2F(0f, 0f), pointCanvasRadius)
    Circle.drawCircleFill(drawInfo, Vec2F(100.0f, 0.0f), pointCanvasRadius)
    Circle.drawCircleFill(drawInfo, Vec2F(1000.0f, 0.0f), pointCanvasRadius)

    drawInfo.context.fillStyle = styleFont
    Text.setFont(drawInfo, FontStyle.STATIC_TINY)
    Text.drawText(drawInfo, Vec2F(0f, 0f), Vec2D(10, 10), "Origin")
    Text.drawText(drawInfo, Vec2F(100f, 0f), Vec2D(10, 10), "100m")
    Text.drawText(drawInfo, Vec2F(1000f, 0f), Vec2D(10, 10), "1,000m")
  }
}
