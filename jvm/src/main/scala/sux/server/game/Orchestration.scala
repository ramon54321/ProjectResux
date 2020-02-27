package sux.server.game

import sux.common.actions.WorldActions
import sux.common.math.{DVec2F, LUTF, Vec2F}
import sux.server.Hub
import sux.server.game.Specs.{Human, Item}
import sux.server.game.movement.Movement

object Orchestration {
  def spawnEntity(id: String, spec: Spec): Unit = {
    val now = System.currentTimeMillis()
    val position = DVec2F(LUTF(now -> 0f, now + 10000L -> 20f), LUTF(now -> 0f, now + 15000L -> 10f))
    spec match {
      case human: Human =>
        Hub.patchWorldState(WorldActions.SpawnHuman(id, spec.tag, position.toSerializable))
        Hub.patchWorldState(WorldActions.SetEntityAttributeString(id, "Name", "Alan P. Wilson"))
        Hub.patchWorldState(WorldActions.SetEntityAttributeInt(id, "Rank", 3))
        Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Health", human.maxHealth))
        Hub.patchWorldState(WorldActions.SetEntityAttributeFloat(id, "Munitions", human.maxStorage))
      case item: Item => Hub.patchWorldState(WorldActions.SpawnItem(id, spec.tag, position.toSerializable))
    }
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

  def addEntityItem(id: String, item: Item): Unit = {
    Hub.worldState.getEntityById(id)
      .foreach(_ => Hub.patchWorldState(WorldActions.AddEntityItem(id, item.name)))
  }

  def removeEntityItem(id: String, item: Item): Unit = {
    Hub.worldState.getEntityById(id)
      .foreach(_ => Hub.patchWorldState(WorldActions.RemoveEntityItem(id, item.name)))
  }

  def teleportEntity(id: String, to: Vec2F): Unit = {
    Hub.patchWorldState(WorldActions.SetEntityPosition(id, to.asDeterministic().toSerializable))
  }

  def moveEntity(id: String, from: Vec2F, to: Vec2F): Unit = {
    val now = System.currentTimeMillis()
    for {
      entity <- Hub.worldState.getEntityById(id)
      path <- Movement.getPath(entity, from, to, now)
    } yield {
      Hub.patchWorldState(WorldActions.SetEntityPosition(id, path.toSerializable))
    }
  }

  def moveEntity(id: String, to: Vec2F): Unit = {
    val now = System.currentTimeMillis()
    for {
      entity <- Hub.worldState.getEntityById(id)
      currentPosition <- Some(entity.position.lookup(now))
      path <- Movement.getPath(entity, currentPosition, to, now)
    } yield {
      Hub.patchWorldState(WorldActions.SetEntityPosition(id, path.toSerializable))
    }
  }
}
