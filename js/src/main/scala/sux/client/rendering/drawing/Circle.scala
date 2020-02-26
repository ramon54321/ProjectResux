package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.FrameInfo
import sux.client.rendering.Mapping._
import sux.common.math.{Rect, Vec2D, Vec2F}

import scala.math.Pi

object Circle {
  def drawCircle(frameInfo: FrameInfo, worldCenter: Vec2F, canvasRadius: Double, ignoreBounds: Boolean = false): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldCenter) && !ignoreBounds) return
    val canvasCenter = worldSpaceToCanvasSpace(frameInfo, worldCenter)
    frameInfo.context.beginPath()
    frameInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    frameInfo.context.stroke()
    RenderStatistics.drawCircleCount += 1
  }

  def drawCircleFill(frameInfo: FrameInfo, worldCenter: Vec2F, canvasRadius: Double, ignoreBounds: Boolean = false): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldCenter) && !ignoreBounds) return
    val canvasCenter = worldSpaceToCanvasSpace(frameInfo, worldCenter)
    frameInfo.context.beginPath()
    frameInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    frameInfo.context.fill()
    RenderStatistics.drawCircleCount += 1
  }

  def drawCircleFillCanvasSpace(frameInfo: FrameInfo, canvasCenter: Vec2D, canvasRadius: Double): Unit = {
    frameInfo.context.beginPath()
    frameInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, 0.0, Pi * 2)
    frameInfo.context.fill()
    RenderStatistics.drawCircleCount += 1
  }

  def drawArc(frameInfo: FrameInfo, worldCenter: Vec2F, worldRadius: Float, startAngle: Float, endAngle: Float): Unit = {
    if (!Rect.isInBounds(frameInfo.screenWorldRect, worldCenter)) return
    val canvasCenter = worldSpaceToCanvasSpace(frameInfo, worldCenter)
    val canvasRadius = worldSpaceToCanvasSpace(frameInfo, worldRadius)
    val canvasStartAngle = mapAngleToCanvas(startAngle)
    val canvasEndAngle = mapAngleToCanvas(endAngle)
    val counterClockwise = canvasStartAngle > canvasEndAngle
    frameInfo.context.beginPath()
    frameInfo.context.arc(canvasCenter.x, canvasCenter.y, canvasRadius, canvasStartAngle.toDouble, canvasEndAngle.toDouble, counterClockwise)
    frameInfo.context.stroke()
    RenderStatistics.drawCircleCount += 1
  }
}
