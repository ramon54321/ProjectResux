package sux.common.actions

import sux.common.serialization.{JsonSerializer, Kinded}

object ClientActions {
  sealed class ClientAction(val kind: String) extends Kinded
  case class Ping(timestamp: Long) extends ClientAction("Ping")

  object Serializer extends JsonSerializer[ClientAction] {
    addClass[Ping]("Ping")
  }
}


