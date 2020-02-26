package sux.client.rendering.layers

import sux.client.debug.Timer
import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.Line._
import sux.common.math.{Spread, Vec2F}

import scala.math.abs

class BackgroundGridRenderLayer extends RenderLayer {

  private val timer = new Timer("Grid")

  private val styleMinor = "rgba(255, 255, 255, 0.04)"
  private val styleMajor = "rgba(255, 255, 255, 0.10)"
  private val styleSuperMajor = "rgba(255, 255, 255, 0.20)"

  private val superMajor = 100f
  private val major = 10f
  private val minor = 2f

  override def draw(frameInfo: FrameInfo) {
    timer.markStart()

    val xStart = Spread.floored(2, frameInfo.screenWorldRect.topLeft.x)
    val xEnd = Spread.ceiled(2, frameInfo.screenWorldRect.bottomRight.x)
    val yStart = Spread.floored(2, frameInfo.screenWorldRect.bottomRight.y)
    val yEnd = Spread.ceiled(2, frameInfo.screenWorldRect.topLeft.y)
    val width = abs(xStart - xEnd)
    val scale = if (width > 250) "Big" else "Small"

    var xCurrent = xStart
    while (xCurrent < xEnd) {
      if (xCurrent % superMajor == 0f) {
        frameInfo.context.strokeStyle = styleSuperMajor
        frameInfo.context.lineWidth = 2.0
        drawLine(frameInfo, Vec2F(xCurrent, yStart), Vec2F(xCurrent, yEnd))
        frameInfo.context.lineWidth = 1.0
      } else if (xCurrent % major == 0f) {
        frameInfo.context.strokeStyle = styleMajor
        drawLine(frameInfo, Vec2F(xCurrent, yStart), Vec2F(xCurrent, yEnd))
      } else if (scale == "Small") {
        frameInfo.context.strokeStyle = styleMinor
        drawLine(frameInfo, Vec2F(xCurrent, yStart), Vec2F(xCurrent, yEnd))
      }
      xCurrent += minor
    }

    var yCurrent = yStart
    while (yCurrent < yEnd) {
      if (yCurrent % superMajor == 0f) {
        frameInfo.context.lineWidth = 2.0
        frameInfo.context.strokeStyle = styleSuperMajor
        drawLine(frameInfo, Vec2F(xStart, yCurrent), Vec2F(xEnd, yCurrent))
        frameInfo.context.lineWidth = 1.0
      } else if (yCurrent % major == 0f) {
        frameInfo.context.strokeStyle = styleMajor
        drawLine(frameInfo, Vec2F(xStart, yCurrent), Vec2F(xEnd, yCurrent))
      } else if (scale == "Small") {
        frameInfo.context.strokeStyle = styleMinor
        drawLine(frameInfo, Vec2F(xStart, yCurrent), Vec2F(xEnd, yCurrent))
      }
      yCurrent += minor
    }

    timer.markEnd()
  }
}
