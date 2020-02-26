package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.{FrameInfo, Mapping}
import sux.common.math.{Rect, Vec2D, Vec2F}

object Text {
  object FontStyle extends Enumeration {
    protected case class Val(style: String) extends super.Val
    type FontStyle = Val
    val STATIC_TINY = Val("12px Lato")
    val STATIC_SMALL = Val("16px Lato")
    val STATIC_MEDIUM = Val("24px Lato")
    val STATIC_LARGE = Val("28px Lato")
  }

  import FontStyle._

  def setFont(frameInfo: FrameInfo, fontStyle: FontStyle): Unit = {
    frameInfo.context.font = fontStyle.style
  }

  def drawText(frameInfo: FrameInfo, worldPosition: Vec2F, text: String): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldPosition)) return
    val canvasPosition = Mapping.worldSpaceToCanvasSpace(frameInfo, worldPosition)
    frameInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }

  def drawText(frameInfo: FrameInfo, worldPosition: Vec2F, canvasOffset: Vec2D, text: String): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldPosition)) return
    val canvasPosition = Vec2F.add(Mapping.worldSpaceToCanvasSpace(frameInfo, worldPosition).asVector2F(), Vec2F.scaleY(canvasOffset.asVector2F(), -1))
    frameInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }

  def drawTextCanvasSpace(frameInfo: FrameInfo, canvasPosition: Vec2D, text: String): Unit = {
    frameInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }
}
