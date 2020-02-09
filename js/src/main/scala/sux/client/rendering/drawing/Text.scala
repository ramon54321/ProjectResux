package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.{DrawInfo, Mapping}
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

  def setFont(drawInfo: DrawInfo, fontStyle: FontStyle): Unit = {
    drawInfo.context.font = fontStyle.style
  }

  def drawText(drawInfo: DrawInfo, worldPosition: Vec2F, text: String): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldPosition)) return
    val canvasPosition = Mapping.worldSpaceToCanvasSpace(drawInfo, worldPosition)
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }

  def drawText(drawInfo: DrawInfo, worldPosition: Vec2F, canvasOffset: Vec2D, text: String): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldPosition)) return
    val canvasPosition = Vec2F.add(Mapping.worldSpaceToCanvasSpace(drawInfo, worldPosition).asVector2F(), Vec2F.scaleY(canvasOffset.asVector2F(), -1))
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }

  def drawTextCanvasSpace(drawInfo: DrawInfo, canvasPosition: Vec2D, text: String): Unit = {
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
    RenderStatistics.drawTextCount += 1
  }
}
