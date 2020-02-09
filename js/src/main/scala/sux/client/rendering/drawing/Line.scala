package sux.client.rendering.drawing

import sux.client.debug.RenderStatistics
import sux.client.rendering.{DrawInfo, Mapping}
import sux.common.math.Vec2F

object Line {
  def drawLine(drawInfo: DrawInfo, worldStart: Vec2F, worldEnd: Vec2F): Unit = {
    val canvasStart = Mapping.worldSpaceToCanvasSpace(drawInfo, worldStart)
    val canvasEnd = Mapping.worldSpaceToCanvasSpace(drawInfo, worldEnd)
    drawInfo.context.beginPath()
    drawInfo.context.moveTo(canvasStart.x, canvasStart.y)
    drawInfo.context.lineTo(canvasEnd.x, canvasEnd.y)
    drawInfo.context.stroke()
    RenderStatistics.drawLineCount += 1
  }

  def drawPath(drawInfo: DrawInfo, worldPoints: List[Vec2F]): Unit = {
    if (worldPoints.size < 2) {
      return
    }
    val canvasPoints = worldPoints.map(worldPoint => Mapping.worldSpaceToCanvasSpace(drawInfo, worldPoint))
    drawInfo.context.beginPath()
    drawInfo.context.moveTo(canvasPoints.head.x, canvasPoints.head.y)
    canvasPoints.tail.foreach(canvasPoint => drawInfo.context.lineTo(canvasPoint.x, canvasPoint.y))
    drawInfo.context.stroke()
    RenderStatistics.drawLineCount += 1
  }
}
