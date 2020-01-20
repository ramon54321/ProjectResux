package sux.client.rendering.drawing

import sux.client.rendering.DrawInfo
import sux.client.rendering.Mapping._
import sux.common.math.Vector2F
import scala.math.Pi

object Circle {
  def drawCircle(drawInfo: DrawInfo, worldCenter: Vector2F, canvasRadius: Double): Unit = {
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    drawInfo.context.stroke()
  }

  def drawCircleFill(drawInfo: DrawInfo, worldCenter: Vector2F, canvasRadius: Double): Unit = {
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    drawInfo.context.fill()
  }

  def drawArc(drawInfo: DrawInfo, worldCenter: Vector2F, worldRadius: Float, startAngle: Float, endAngle: Float): Unit = {
    val canvasCenter = worldSpaceToCanvasSpace(drawInfo, worldCenter)
    val canvasRadius = worldSpaceToCanvasSpace(drawInfo, worldRadius)
    val canvasStartAngle = mapAngleToCanvas(startAngle)
    val canvasEndAngle = mapAngleToCanvas(endAngle)
    val counterClockwise = canvasStartAngle > canvasEndAngle
    drawInfo.context.beginPath()
    drawInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, canvasStartAngle.toDouble, canvasEndAngle.toDouble, counterClockwise)
    drawInfo.context.stroke()
  }
}
