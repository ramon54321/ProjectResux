package sux.client.rendering.layers

import sux.client.InterfaceState
import sux.client.debug.Timer
import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.{Vec2D, Vec2F}

class ContextMenuRenderLayer extends RenderLayer {

  private val timer = new Timer("ContextMenu")

  private val nodeStyle = "rgba(255, 255, 255, 0.75)"
  private val nodeStyleHover = "rgba(255, 255, 255, 0.9)"
  private val nodeTextStyle = "rgba(0, 122, 204, 1.0)"
  private val centerStyle = "rgba(255, 255, 255, 0.75)"

  private val radialSpread = 40.0
  private val nodeRadius = 24.0
  private val nodeSquareRadius = nodeRadius * nodeRadius

  private def drawText(frameInfo: FrameInfo, canvasCenterPosition: Vec2D, text: String): Unit = {
    frameInfo.context.fillStyle = nodeTextStyle
    val texts = text.split(' ')
    val positions = texts.zipWithIndex.map(textWithIndex => canvasCenterPosition + Vec2D(0, 4 - (6 * (texts.length - 1)) + 12 * textWithIndex._2))
    texts.zip(positions).foreach(textWithPosition => Text.drawTextCanvasSpace(frameInfo, textWithPosition._2, textWithPosition._1.toUpperCase))
  }

  override def draw(frameInfo: FrameInfo) {
    timer.markStart()

    frameInfo.context.textAlign = "center"
    if (InterfaceState.getIsContextMenuOpen) {
      val contextMenu = InterfaceState.getContextMenu
      contextMenu.right.foreach(root => {
        val childCount = root.children.length
        val divisions =
          if (childCount <= 4) childCount
          else if (childCount <= 6) 6
          else if (childCount <= 8) 8
          else 12
        val spreadMultiplier =
          if (childCount <= 4) 1f
          else if (childCount <= 6) 1.4f
          else if (childCount <= 8) 1.65f
          else 2f
        val angleDelta = 2 * math.Pi / divisions
        val nodesWithIndex = root.children.zipWithIndex
        val mouseCanvasPosition = frameInfo.mouseCanvasPosition
        val contextMenuCanvasPosition = InterfaceState.getContextMenuCanvasCenter
        val canvasCenterPositionWithNodes = nodesWithIndex.map(nodeWithIndex => {
          val index = nodeWithIndex._2
          val angle = (-angleDelta * index) + math.Pi / 2
          val x = math.cos(angle) * radialSpread * spreadMultiplier + contextMenuCanvasPosition.x
          val y = math.sin(angle) * radialSpread * spreadMultiplier + contextMenuCanvasPosition.y
          (Vec2D(x, y), nodeWithIndex._1)
        })
        val hoverMap = canvasCenterPositionWithNodes.map(canvasCenterPositionWithNode => {
          val canvasCenterPosition = canvasCenterPositionWithNode._1
          val node = canvasCenterPositionWithNode._2
          val isHovering = Vec2F.squareDistance(mouseCanvasPosition.asVector2F(), canvasCenterPosition.asVector2F()) < nodeSquareRadius
          if (isHovering) InterfaceState.setHoverNode(node)
          frameInfo.context.fillStyle = if (isHovering) nodeStyleHover else nodeStyle
          Circle.drawCircleFillCanvasSpace(frameInfo, canvasCenterPosition, nodeRadius)
          drawText(frameInfo, canvasCenterPosition, node.name)
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
