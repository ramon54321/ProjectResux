package sux.client.rendering.layers

import sux.client.{ContextMenuNode, InterfaceState}
import sux.client.debug.Timer
import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.{Vec2D, Vec2F}

class ContextMenuRenderLayer extends RenderLayer {

  private val timer = new Timer("ContextMenu")

  private val nodeStyle = "rgba(255, 255, 255, 0.5)"
  private val nodeStyleHover = "rgba(255, 255, 255, 0.9)"
  private val nodeTextStyle = "rgba(0, 122, 204, 1.0)"

  private val radialSpread = 40.0
  private val nodeRadius = 24.0
  private val nodeSquareRadius = nodeRadius * nodeRadius

  override def draw(drawInfo: DrawInfo) {
    timer.markStart()
    drawInfo.context.save()

    drawInfo.context.textAlign = "center"
    InterfaceState.getContextMenuNode.foreach(rootNode => {
      val childCount = rootNode.children.length
      val divisions =
        if (childCount <= 4) childCount
        else if (childCount <= 8) 8
        else 12
      val angleDelta = 2 * math.Pi / divisions
      val nodesWithIndex = rootNode.children.zipWithIndex
      val mouseCanvasPosition = InterfaceState.getMouseCanvasPosition
      val contextMenuCanvasPosition = InterfaceState.getContextMenuCanvasCenter
      val canvasCenterPositionWithNodes = nodesWithIndex.map(nodeWithIndex => {
        val index = nodeWithIndex._2
        val angle = (-angleDelta * index) + math.Pi / 2
        val x = math.cos(angle) * radialSpread + contextMenuCanvasPosition.x
        val y = math.sin(angle) * radialSpread + contextMenuCanvasPosition.y
        (Vec2D(x, y), nodeWithIndex._1)
      })
      val hoverMap = canvasCenterPositionWithNodes.map(canvasCenterPositionWithNode => {
        val canvasCenterPosition = canvasCenterPositionWithNode._1
        val node = canvasCenterPositionWithNode._2
        val isHovering = Vec2F.squareDistance(mouseCanvasPosition.asVector2F(), canvasCenterPosition.asVector2F()) < nodeSquareRadius
        if (isHovering) InterfaceState.setContextMenuHoverNode(node)
        drawInfo.context.fillStyle = if (isHovering) nodeStyleHover else nodeStyle
        Circle.drawCircleFillCanvasSpace(drawInfo, canvasCenterPosition, nodeRadius)
        drawInfo.context.fillStyle = nodeTextStyle
        Text.drawTextCanvasSpace(drawInfo, canvasCenterPosition + Vec2D(0, 4), node.name.toUpperCase)
        isHovering
      })
      if (!hoverMap.contains(true)) InterfaceState.clearContextMenuHoverNode()
    })

    drawInfo.context.restore()
    timer.markEnd()
  }
}
