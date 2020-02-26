package sux.client

import sux.client.gameplay.ContextMenu
import sux.client.rendering.FrameInfo
import sux.common.state.Entity

object InterfaceOrchestration {
  def toggleContextMenu(frameInfo: FrameInfo): Unit = {
    if (InterfaceState.getIsContextMenuOpen) InterfaceState.closeContextMenu()
    else {
      InterfaceState.setContextMenu(ContextMenu.getContextMenu)
      InterfaceState.setContextMenuCanvasCenter(frameInfo.mouseCanvasPosition)
      InterfaceState.openContextMenu()
    }
  }
  def deselectAll(): Unit = {
    InterfaceState.closeContextMenu()
    InterfaceState.clearContextMenu()
    InterfaceState.clearHoverNode()
    InterfaceState.clearSelectedEntity()
  }
  def clickNode(node: ContextMenuNode): Unit = {
    if (node.children.nonEmpty) InterfaceState.setContextMenu(Right(node))
    else {
      println(node.name)
      deselectAll()
    }
  }
  def clickEntity(entity: Entity): Unit = {
    InterfaceState.setSelectedEntity(entity)
  }
}
