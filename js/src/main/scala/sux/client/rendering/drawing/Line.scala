package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.{FrameInfo, Mapping}
import sux.common.math.Vec2F

object Line {
  def drawLine(frameInfo: FrameInfo, worldStart: Vec2F, worldEnd: Vec2F): Unit = {
    val canvasStart = Mapping.worldSpaceToCanvasSpace(frameInfo, worldStart)
    val canvasEnd = Mapping.worldSpaceToCanvasSpace(frameInfo, worldEnd)
    frameInfo.context.beginPath()
    frameInfo.context.moveTo(canvasStart.x, canvasStart.y)
    frameInfo.context.lineTo(canvasEnd.x, canvasEnd.y)
    frameInfo.context.stroke()
    RenderStatistics.drawLineCount += 1
  }

  def drawPath(frameInfo: FrameInfo, worldPoints: List[Vec2F]): Unit = {
    if (worldPoints.size < 2) {
      return
    }
    val canvasPoints = worldPoints.map(worldPoint => Mapping.worldSpaceToCanvasSpace(frameInfo, worldPoint))
    frameInfo.context.beginPath()
    frameInfo.context.moveTo(canvasPoints.head.x, canvasPoints.head.y)
    canvasPoints.tail.foreach(canvasPoint => frameInfo.context.lineTo(canvasPoint.x, canvasPoint.y))
    frameInfo.context.stroke()
    RenderStatistics.drawLineCount += 1
  }
}
