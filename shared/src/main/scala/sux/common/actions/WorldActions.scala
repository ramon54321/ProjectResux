package sux.common.actions

import sux.common.math.DVec2F
import sux.common.serialization.{JsonSerializer, Kinded}

object WorldActions {
  sealed abstract class WorldAction(val kind: String) extends Kinded
  case class Signal(code: Int) extends WorldAction("Signal")
  case class Ping(timestamp: Long) extends WorldAction("Ping")
  case class SpawnHuman(id: String, specTag: String, position: DVec2F.Serializable) extends WorldAction("SpawnHuman")
  case class SpawnItem(id: String, specTag: String, position: DVec2F.Serializable) extends WorldAction("SpawnItem")
  case class SetEntityPosition(id: String, position: DVec2F.Serializable) extends WorldAction("SetEntityPosition")
  case class SetEntityAttributeString(id: String, name: String, value: String) extends WorldAction("SetEntityAttributeString")
  case class SetEntityAttributeFloat(id: String, name: String, value: Float) extends WorldAction("SetEntityAttributeFloat")
  case class SetEntityAttributeInt(id: String, name: String, value: Int) extends WorldAction("SetEntityAttributeInt")
  case class AddEntityItem(id: String, name: String) extends WorldAction("AddEntityItem")
  case class RemoveEntityItem(id: String, name: String) extends WorldAction("RemoveEntityItem")

  object Serializer extends JsonSerializer[WorldAction] {
    addClass[Signal]("Signal")
    addClass[Ping]("Ping")
    addClass[SpawnHuman]("SpawnHuman")
    addClass[SpawnItem]("SpawnItem")
    addClass[SetEntityPosition]("SetEntityPosition")
    addClass[SetEntityAttributeString]("SetEntityAttributeString")
    addClass[SetEntityAttributeFloat]("SetEntityAttributeFloat")
    addClass[SetEntityAttributeInt]("SetEntityAttributeInt")
    addClass[AddEntityItem]("AddEntityItem")
    addClass[RemoveEntityItem]("RemoveEntityItem")
  }
}
