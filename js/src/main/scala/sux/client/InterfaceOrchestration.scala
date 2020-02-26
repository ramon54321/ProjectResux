package sux.client

import sux.client.InterfaceState.getMouseCanvasPosition
import sux.client.gameplay.ContextMenu
import sux.common.state.Entity

object InterfaceOrchestration {
  def toggleContextMenu(): Unit = {
    if (InterfaceState.getIsContextMenuOpen) InterfaceState.closeContextMenu()
    else {
      InterfaceState.setContextMenu(ContextMenu.getContextMenu)
      InterfaceState.setContextMenuCanvasCenter(getMouseCanvasPosition.asImmutable())
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
