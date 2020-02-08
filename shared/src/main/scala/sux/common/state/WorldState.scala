package sux.common.state

import sux.common.actions.WorldActions._
import sux.common.math.{LUT, Vector2F}

import scala.collection.mutable

trait Deterministic[T] {
  def lookup(x: Long): T
}

class DeterministicVector2F extends Deterministic[Vector2F] {
  private val now = System.currentTimeMillis()
  private val x = LUT(now -> 0f, now + 10000L -> 20f)
  private val y = LUT(now -> 0f, now + 15000L -> 10f)
  override def lookup(t: Long): Vector2F = Vector2F(x.lookup(t), y.lookup(t))
}

class Entity(val id: String, val name: String) {
  val position: DeterministicVector2F = new DeterministicVector2F
}

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
    case Spawn(id, name) => entitiesById.put(id, new Entity(id, name))
  }

  private val entitiesById = new mutable.HashMap[String, Entity]()

  def entities: Iterable[Entity] = entitiesById.values
}
