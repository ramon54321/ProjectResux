package sux.client.gameplay

import sux.client.{ContextMenuNode, InterfaceState}
import sux.common.enums.Enums.MoveSpeed

object ContextMenu {
  def getContextMenu: Either[String, ContextMenuNode] = {
    val selectedEntity = InterfaceState.getSelectedEntity
    if (selectedEntity.isDefined) {
      Right(createContextMenu(
        new ContextMenuNode("Slow Move", frameInfo => Orchestration.moveEntity(selectedEntity.get, frameInfo.mouseWorldPosition, MoveSpeed.Slow)),
        new ContextMenuNode("Move", frameInfo => Orchestration.moveEntity(selectedEntity.get, frameInfo.mouseWorldPosition, MoveSpeed.Normal)),
        new ContextMenuNode("Fast Move", frameInfo => Orchestration.moveEntity(selectedEntity.get, frameInfo.mouseWorldPosition, MoveSpeed.Fast)),
        new ContextMenuNode("Debug", _ => Unit,
          new ContextMenuNode("Entity", frameInfo => println(selectedEntity.get.toString(frameInfo.worldTime))),
          new ContextMenuNode("Spec Tag", _ => println(selectedEntity.get.specTag)),
          new ContextMenuNode("C", _ => Unit)
        ),
      ))
    } else {
      Left("No Entity Selected")
    }
  }

  private def createContextMenu(options: ContextMenuNode*) = new ContextMenuNode("Root", _ => Unit, options:_*)
}
