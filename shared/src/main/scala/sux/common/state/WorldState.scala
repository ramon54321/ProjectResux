package sux.common.state

import sux.common.actions.WorldActions._

class WorldState {
  def patch(action: WorldAction): Unit = action match {
    case Signal(code) => println(s"Patching with Signal ${code}")
    case Ping(timestamp) => println(s"Patching with Ping $timestamp")
  }
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