package sux.client

import sux.client.gameplay.ContextMenu
import sux.client.rendering.FrameInfo
import sux.common.state.Entity

object InterfaceOrchestration {
  def openContextMenu(frameInfo: FrameInfo): Unit = {
    InterfaceState.setContextMenu(ContextMenu.getContextMenu)
    InterfaceState.setContextMenuCanvasCenter(frameInfo.mouseCanvasPosition)
    InterfaceState.openContextMenu()
  }
  def closeContextMenu(): Unit = {
    InterfaceState.closeContextMenu()
    InterfaceState.clearContextMenu()
    InterfaceState.clearHoverNode()
  }
  def toggleContextMenu(frameInfo: FrameInfo): Unit = {
    if (InterfaceState.getIsContextMenuOpen) closeContextMenu()
    else openContextMenu(frameInfo)
  }
  def deselectAll(): Unit = {
    closeContextMenu()
    InterfaceState.clearSelectedEntity()
  }
  def clickNode(frameInfo: FrameInfo, node: ContextMenuNode): Unit = {
    if (node.children.nonEmpty) InterfaceState.setContextMenu(Right(node))
    else {
      node.action(frameInfo)
      closeContextMenu()
    }
  }
  def clickEntity(frameInfo: FrameInfo, entity: Entity): Unit = {
    InterfaceState.setSelectedEntity(entity)
    if (InterfaceState.getIsContextMenuOpen) closeContextMenu()
  }
}
