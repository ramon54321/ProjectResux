package sux.common.actions

import sux.common.serialization.{JsonSerializer, Kinded}

object WorldActions {
  sealed class WorldAction(val kind: String) extends Kinded
  case class Signal(code: Int) extends WorldAction("Signal")
  case class Spawn(id: Int, name: String) extends WorldAction("Spawn")
  case class Ping(timestamp: Long) extends WorldAction("Ping")

  object Serializer extends JsonSerializer[WorldAction] {
    addClass[Signal]("Signal")
    addClass[Spawn]("Spawn")
    addClass[Ping]("Ping")
  }
}
