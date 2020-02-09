package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.DrawInfo
import sux.client.rendering.Mapping._
import sux.common.math.{Rect, Vec2F}

import scala.math.Pi

object Circle {
  def drawCircle(drawInfo: DrawInfo, worldCenter: Vec2F, canvasRadius: Double, ignoreBounds: Boolean = false): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldCenter) && !ignoreBounds) return
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    drawInfo.context.stroke()
    RenderStatistics.drawCircleCount += 1
  }

  def drawCircleFill(drawInfo: DrawInfo, worldCenter: Vec2F, canvasRadius: Double, ignoreBounds: Boolean = false): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldCenter) && !ignoreBounds) return
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    drawInfo.context.fill()
    RenderStatistics.drawCircleCount += 1
  }

  def drawArc(drawInfo: DrawInfo, worldCenter: Vec2F, worldRadius: Float, startAngle: Float, endAngle: Float): Unit = {
    if (!Rect.isInBounds(drawInfo.screenWorldRect, worldCenter)) return
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    val canvasRadius = worldSpaceToCanvasSpace(drawInfo, worldRadius)
    val canvasStartAngle = mapAngleToCanvas(startAngle)
    val canvasEndAngle = mapAngleToCanvas(endAngle)
    val counterClockwise = canvasStartAngle > canvasEndAngle
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, canvasStartAngle.toDouble, canvasEndAngle.toDouble, counterClockwise)
    drawInfo.context.stroke()
    RenderStatistics.drawCircleCount += 1
  }
}
