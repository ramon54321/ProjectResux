package sux.client.gameplay

import sux.client.Hub
import sux.common.actions.ClientActions
import sux.common.enums.Enums.MoveSpeed.MoveSpeed
import sux.common.math.Vec2F
import sux.common.state.Entity

object Orchestration {
  def moveEntity(entity: Entity, position: Vec2F, moveSpeed: MoveSpeed): Unit = {
    Hub.dispatch(ClientActions.MoveEntity(entity.id, position.toSerializable, moveSpeed))
  }

  def stopEntity(entity: Entity): Unit = {
    Hub.dispatch(ClientActions.StopEntity(entity.id))
  }
}
