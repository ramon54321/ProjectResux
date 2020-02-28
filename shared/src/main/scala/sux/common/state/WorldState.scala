package sux.common.state

import sux.common.actions.WorldActions._
import sux.common.math.{DVec2F, Vec2F}

import scala.collection.mutable

trait Attributes {
  val attributes = new mutable.HashMap[String, Any]
  def setAttribute(name: String, value: Any): Unit = attributes.put(name, value)
  def getAttribute[T](name: String): Option[T] = attributes.get(name).asInstanceOf[Option[T]]
}

trait Inventory {
  val items = new mutable.ListBuffer[String]
  def addItem(name: String): Unit = items.append(name)
  def removeItem(name: String): Unit = items -= name
}

abstract class Entity(val id: String, val specTag: String, var position: DVec2F) extends Attributes {
  def toString(time: Long): String = s"ID:   $id\nPOS:  ${position.lookup(time)}"
}

class Human(id: String, spec: String, position: DVec2F) extends Entity(id, spec, position) with Inventory {

}

class Item(id: String, spec: String, position: DVec2F) extends Entity(id, spec, position) {

}

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
    case Sync(offset, delay) => {
      val syncEnd = System.currentTimeMillis()
      val syncDuration = syncEnd - syncStart
      val ping = (syncDuration - delay) / 2
      timeCorrection = offset - ping
      println(timeCorrection)
    }

    case SpawnHuman(id, specTag, position) => entitiesById.put(id, new Human(id, specTag, DVec2F.fromSerializable(position)))
    case SpawnItem(id, specTag, position) => entitiesById.put(id, new Item(id, specTag, DVec2F.fromSerializable(position)))
    case SetEntityPosition(id, position) => entitiesById(id).position = DVec2F.fromSerializable(position)

    case SetEntityAttributeString(id, name, value) => entitiesById(id).setAttribute(name, value)
    case SetEntityAttributeFloat(id, name, value) => entitiesById(id).setAttribute(name, value)
    case SetEntityAttributeInt(id, name, value) => entitiesById(id).setAttribute(name, value)

    case AddEntityItem(id, name) => entitiesById(id).asInstanceOf[Human].addItem(name)
    case RemoveEntityItem(id, name) => entitiesById(id).asInstanceOf[Human].removeItem(name)

    case _ => println(s"[CRITICAL] Unknown WorldAction Received - $action")
  }

  var syncStart = 0L
  var timeCorrection = 0L
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
