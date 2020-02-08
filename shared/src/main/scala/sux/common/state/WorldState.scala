package sux.common.state

import sux.common.actions.WorldActions._
import sux.common.math.DeterministicVector2F

import scala.collection.mutable

class Entity(val id: String, val name: String, var position: DeterministicVector2F)

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
    case SpawnEntity(id, name, positionSerializable) => entitiesById.put(id, new Entity(id, name, DeterministicVector2F.fromSerializable(positionSerializable)))
    case SetEntityPosition(id, positionSerializable) => entitiesById(id).position = DeterministicVector2F.fromSerializable(positionSerializable)
  }

  private val entitiesById = new mutable.HashMap[String, Entity]()

  def entities: Iterable[Entity] = entitiesById.values
}
