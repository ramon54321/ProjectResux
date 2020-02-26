package sux.common.state

import sux.common.actions.WorldActions._
import sux.common.math.{DVec2F, Vec2F}

import scala.collection.mutable

class Entity(val id: String, var position: DVec2F) {
  val attributes = new mutable.HashMap[String, Any]
  def setAttribute(name: String, value: Any): Unit = attributes.put(name, value)
  def getAttribute[T](name: String): Option[T] = attributes.get(name).asInstanceOf[Option[T]]

  def toString(time: Long): String = s"ID:   $id\nPOS:  ${position.lookup(time)}"
}

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
    case SpawnEntity(id, position) => entitiesById.put(id, new Entity(id, DVec2F.fromSerializable(position)))
    case SetEntityPosition(id, position) => entitiesById(id).position = DVec2F.fromSerializable(position)
    case SetEntityAttributeString(id, name, value) => entitiesById(id).setAttribute(name, value)
    case SetEntityAttributeFloat(id, name, value) => entitiesById(id).setAttribute(name, value)
    case SetEntityAttributeInt(id, name, value) => entitiesById(id).setAttribute(name, value)
    case _ => println(s"[CRITICAL] Unknown WorldAction Received - $action")
  }

  private val entitiesById = new mutable.HashMap[String, Entity]()

  def entities: Iterable[Entity] = entitiesById.values

  def getEntityById(id: String): Option[Entity] = entitiesById.get(id)
  def getEntityByNearest(position: Vec2F, t: Long): Option[Entity] = {
    if (entities.isEmpty) return None
    var currentNearestDistance = Float.MaxValue
    var currentNearestEntity: Entity = entities.head
    entities.foreach(entity => {
      val distance = Vec2F.squareDistance(position, entity.position.lookup(t))
      if (distance < currentNearestDistance) {
        currentNearestEntity = entity
        currentNearestDistance = distance
      }
    })
    Some(currentNearestEntity)
  }
}
