package sux.server.game

import sux.common.actions.WorldActions
import sux.common.math.{DVec2F, LUTF, Vec2F}
import sux.server.Hub

object Orchestration {
  def spawnEntity(id: String): Unit = {
    val now = System.currentTimeMillis()
    val position = DVec2F(LUTF(now -> 0f, now + 10000L -> 20f), LUTF(now -> 0f, now + 15000L -> 10f))
    Hub.patchWorldState(WorldActions.SpawnEntity(id, position.toSerializable))
    Hub.patchWorldState(WorldActions.SetEntityAttributeString(id, "Name", "Alan P. Wilson"))
    Hub.patchWorldState(WorldActions.SetEntityAttributeInt(id, "Rank", 3))
    Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Health", 100f))
    Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Munitions", 100f))
  }

  // TODO: Split into layers -> entityId or pure entity as parameter?

  def shootEntity(idShooter: String, idTarget: String): Unit = {
    for {
      shooter <- Hub.worldState.getEntityById(idShooter)
      target <- Hub.worldState.getEntityById(idTarget)
      shooterMunitions <- shooter.getAttribute[Float]("Munitions")
      targetHealth <- target.getAttribute[Float]("Health")
      munitionUsage <- Some(21f)
      damage <- Some(55f)
    } yield {
      Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(shooter.id, "Munitions", shooterMunitions - munitionUsage))
      // TODO: Use lower level damage method
      Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(target.id, "Health", targetHealth - damage))
    }
  }

  def damageEntity(id: String, amount: Float): Unit = {
    Hub.worldState.getEntityById(id)
      .flatMap(entity => entity.getAttribute[Float]("Health"))
      .foreach(health => Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Health", health - amount)))
  }

  def healEntity(id: String, amount: Float): Unit = {
    Hub.worldState.getEntityById(id)
      .flatMap(entity => entity.getAttribute[Float]("Health"))
      .foreach(health => Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Health", health + amount)))
  }

  def teleportEntity(id: String, to: Vec2F): Unit = {
    Hub.patchWorldState(WorldActions.SetEntityPosition(id, to.asDeterministic().toSerializable))
  }

  def moveEntity(id: String, from: Vec2F, to: Vec2F): Unit = {
    val now = System.currentTimeMillis()
    Hub.worldState.getEntityById(id)
      .foreach(_ => Hub.patchWorldState(WorldActions.SetEntityPosition(id, DVec2F(now -> from, now + 8000 -> to).toSerializable)))
  }

  def moveEntity(id: String, to: Vec2F): Unit = {
    val now = System.currentTimeMillis()
    Hub.worldState.getEntityById(id)
      .map(entity => entity.position.lookup(now))
      .foreach(currentPosition => Hub.patchWorldState(WorldActions.SetEntityPosition(id, DVec2F(now -> currentPosition, now + 8000 -> to).toSerializable)))
  }
}
