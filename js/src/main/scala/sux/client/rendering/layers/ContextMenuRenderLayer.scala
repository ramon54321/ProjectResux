package sux.client.rendering.layers

import sux.client.InterfaceState
import sux.client.debug.Timer
import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.{Vec2D, Vec2F}

class ContextMenuRenderLayer extends RenderLayer {

  private val timer = new Timer("ContextMenu")

  private val nodeStyle = "rgba(255, 255, 255, 0.5)"
  private val nodeStyleHover = "rgba(255, 255, 255, 0.9)"
  private val nodeTextStyle = "rgba(0, 122, 204, 1.0)"
  private val centerStyle = "rgba(255, 255, 255, 0.6)"

  private val radialSpread = 40.0
  private val nodeRadius = 24.0
  private val nodeSquareRadius = nodeRadius * nodeRadius

  override def draw(frameInfo: FrameInfo) {
    timer.markStart()

    frameInfo.context.textAlign = "center"
    if (InterfaceState.getIsContextMenuOpen) {
      val contextMenu = InterfaceState.getContextMenu
      contextMenu.right.foreach(root => {
        val childCount = root.children.length
        val divisions =
          if (childCount <= 4) childCount
          else if (childCount <= 8) 8
          else 12
        val angleDelta = 2 * math.Pi / divisions
        val nodesWithIndex = root.children.zipWithIndex
        val mouseCanvasPosition = frameInfo.mouseCanvasPosition
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
          if (isHovering) InterfaceState.setHoverNode(node)
          frameInfo.context.fillStyle = if (isHovering) nodeStyleHover else nodeStyle
          Circle.drawCircleFillCanvasSpace(frameInfo, canvasCenterPosition, nodeRadius)
          frameInfo.context.fillStyle = nodeTextStyle
          Text.drawTextCanvasSpace(frameInfo, canvasCenterPosition + Vec2D(0, 4), node.name.toUpperCase)
          isHovering
        })
        frameInfo.context.fillStyle = centerStyle
        Circle.drawCircleFillCanvasSpace(frameInfo, contextMenuCanvasPosition, 2)
        if (!hoverMap.contains(true)) InterfaceState.clearHoverNode()
      })
      contextMenu.left.foreach(error => {
        val contextMenuCanvasPosition = InterfaceState.getContextMenuCanvasCenter
        Text.drawTextCanvasSpace(frameInfo, contextMenuCanvasPosition + Vec2D(0, 4), error)
      })
    }

    timer.markEnd()
  }
}
