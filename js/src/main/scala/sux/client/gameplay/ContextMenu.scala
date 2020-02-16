package sux.client.gameplay

import sux.client.{ContextMenuNode, InterfaceState}

object ContextMenu {
  def getContextMenu: Either[String, ContextMenuNode] = {
    val selectedEntity = InterfaceState.getSelectedEntity
    if (selectedEntity.isDefined) {
      Right(createContextMenu(
        new ContextMenuNode("Move"),
        new ContextMenuNode("Build",
          new ContextMenuNode("Rally Point"),
          new ContextMenuNode("Sandbags"),
          new ContextMenuNode("Barbed Wire")
        ),
        new ContextMenuNode("Attack")
      ))
    } else {
      Left("No Entity Selected")
    }
  }

  private def createContextMenu(options: ContextMenuNode*) = new ContextMenuNode("Root", options:_*)
}
