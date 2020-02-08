package sux.common.actions

import sux.common.serialization.{JsonSerializer, Kinded}

object ClientActions {
  sealed abstract class ClientAction(val kind: String) extends Kinded
  case class Ping(timestamp: Long) extends ClientAction("Ping")
  case class FullStateUpdate() extends ClientAction("FullStateUpdate")

  object Serializer extends JsonSerializer[ClientAction] {
    addClass[Ping]("Ping")
    addClass[FullStateUpdate]("FullStateUpdate")
  }
}


