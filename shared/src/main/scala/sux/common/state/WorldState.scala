package sux.common.state

import sux.common.actions.WorldActions._
import sux.common.math.{LUT, Vector2F}

import scala.collection.mutable

trait Deterministic[T] {
  def lookup(x: Long): T
}

class DeterministicVector2F extends Deterministic[Vector2F] {
  private val x = LUT(0L -> 10f)
  private val y = LUT(0L -> 10f)
  override def lookup(t: Long): Vector2F = Vector2F(x.lookup(t), y.lookup(t))
}

class Entity(val id: String) {
  val position: DeterministicVector2F = new DeterministicVector2F
}

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
  }

  private val entitiesById = new mutable.HashMap[String, Entity]()
}

//
//class WorldState : DiscreteState<WorldAction> {
//
//
//  private val entitiesById = HashMap<String, Entity>()
//
//  override fun patch(action: WorldAction): Boolean {
//  when (action.type) {
//  "Signal" -> {
//
//}
//  "Spawn" -> {
//  val newEntity = Unit()
//  entitiesById[newEntity.getId()] = newEntity
//}
//  else -> return false
//}
//  return true
//}
//
//  fun getEntities(): Array<Entity> {
//  return entitiesById.values.toTypedArray()
//}
//
//  fun getEntityById(id: String): Entity? {
//  return entitiesById[id]
//}
//}