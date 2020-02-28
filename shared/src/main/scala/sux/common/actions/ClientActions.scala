package sux.common.actions

import sux.common.enums.Enums.MoveSpeed.MoveSpeed
import sux.common.math.Vec2F
import sux.common.serialization.{JsonSerializer, Kinded}

object ClientActions {
  sealed abstract class ClientAction(val kind: String) extends Kinded
  case class Ping(timestamp: Long) extends ClientAction("Ping")
  case class Sync(timestamp: Long) extends ClientAction("Sync")
  case class FullStateUpdate() extends ClientAction("FullStateUpdate")
  case class MoveEntity(entityId: String, position: Vec2F.Serializable, moveSpeed: MoveSpeed) extends ClientAction("MoveEntity")
  case class StopEntity(entityId: String) extends ClientAction("StopEntity")

  object Serializer extends JsonSerializer[ClientAction] {
    addClass[Ping]("Ping")
    addClass[Sync]("Sync")
    addClass[FullStateUpdate]("FullStateUpdate")
    addClass[MoveEntity]("MoveEntity")
    addClass[StopEntity]("StopEntity")
  }
}


