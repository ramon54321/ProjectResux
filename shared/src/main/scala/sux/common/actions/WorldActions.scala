package sux.common.actions

import sux.common.math.DeterministicVector2F
import sux.common.serialization.{JsonSerializer, Kinded}

object WorldActions {
  sealed abstract class WorldAction(val kind: String) extends Kinded
  case class Signal(code: Int) extends WorldAction("Signal")
  case class Ping(timestamp: Long) extends WorldAction("Ping")
  case class SpawnEntity(id: String, name: String, positionSerializable: DeterministicVector2F.Serializable) extends WorldAction("SpawnEntity")
  case class SetEntityPosition(id: String, positionSerializable: DeterministicVector2F.Serializable) extends WorldAction("SetEntityPosition")

  object Serializer extends JsonSerializer[WorldAction] {
    addClass[Signal]("Signal")
    addClass[Ping]("Ping")
    addClass[SpawnEntity]("SpawnEntity")
    addClass[SetEntityPosition]("SetEntityPosition")
  }
}
