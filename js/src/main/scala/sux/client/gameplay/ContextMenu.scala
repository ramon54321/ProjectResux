package sux.client.gameplay

import sux.client.rendering.FrameInfo
import sux.client.{ContextMenuNode, InterfaceState}

object ContextMenu {
  def getContextMenu: Either[String, ContextMenuNode] = {
    val selectedEntity = InterfaceState.getSelectedEntity
    if (selectedEntity.isDefined) {
      Right(createContextMenu(
        new ContextMenuNode("Move", (frameInfo: FrameInfo) => Orchestration.moveEntity(
          InterfaceState.getSelectedEntity.get,
          frameInfo.mouseWorldPosition
        )),
        new ContextMenuNode("Build", _ => Unit,
          new ContextMenuNode("Rally Point", _ => Unit),
          new ContextMenuNode("Sandbags", _ => Unit),
          new ContextMenuNode("Barbed Wire", _ => Unit)
        ),
        new ContextMenuNode("Attack", _ => Unit)
      ))
    } else {
      Left("No Entity Selected")
    }
  }

  private def createContextMenu(options: ContextMenuNode*) = new ContextMenuNode("Root", _ => Unit, options:_*)
}
