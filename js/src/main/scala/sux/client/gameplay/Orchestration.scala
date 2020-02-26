package sux.client.gameplay

import sux.client.Hub
import sux.common.actions.ClientActions
import sux.common.math.Vec2F
import sux.common.state.Entity

object Orchestration {
  def moveEntity(entity: Entity, position: Vec2F): Unit = {
    Hub.dispatch(ClientActions.MoveEntity(entity.id, position.toSerializable))
  }
}
