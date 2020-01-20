package sux.client.rendering.drawing

import sux.client.rendering.{DrawInfo, Mapping}
import sux.common.math.{Vector2D, Vector2F}

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

  def drawText(drawInfo: DrawInfo, worldPosition: Vector2F, text: String): Unit = {
    val canvasPosition = Mapping.worldSpaceToCanvasSpace(drawInfo, worldPosition)
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
  }

  def drawText(drawInfo: DrawInfo, worldPosition: Vector2F, canvasOffset: Vector2D, text: String): Unit = {
    val canvasPosition = Vector2F.add(Mapping.worldSpaceToCanvasSpace(drawInfo, worldPosition).asVector2F(), Vector2F.scaleY(canvasOffset.asVector2F(), -1))
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
  }

  def drawTextCanvasSpace(drawInfo: DrawInfo, canvasPosition: Vector2D, text: String): Unit = {
    drawInfo.context.fillText(text, canvasPosition.x, canvasPosition.y)
  }
}
