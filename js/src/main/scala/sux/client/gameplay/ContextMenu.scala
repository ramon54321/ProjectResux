package sux.client.gameplay

import sux.client.{ContextMenuNode, InterfaceState}

object ContextMenu {
  def getContextMenu: Either[String, ContextMenuNode] = {
    val selectedEntity = InterfaceState.getSelectedEntity
    if (selectedEntity.isDefined) {
      Right(createContextMenu(
        new ContextMenuNode("Move", frameInfo => Orchestration.moveEntity(selectedEntity.get, frameInfo.mouseWorldPosition)),
        new ContextMenuNode("Debug", _ => Unit,
          new ContextMenuNode("Entity", frameInfo => println(selectedEntity.get.toString(frameInfo.worldTime))),
          new ContextMenuNode("B", _ => Unit),
          new ContextMenuNode("C", _ => Unit)
        ),
      ))
    } else {
      Left("No Entity Selected")
    }
  }

  private def createContextMenu(options: ContextMenuNode*) = new ContextMenuNode("Root", _ => Unit, options:_*)
}
