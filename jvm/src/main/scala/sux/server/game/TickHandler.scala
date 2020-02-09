package sux.server.game

import sux.common.math.Vec2F

object TickHandler {
  def tick(tickCount: Int): Unit = tickCount match {
    case 10 => Orchestration.spawnEntity("myEntity")
    case 15 => Orchestration.damageEntity("myEntity", 21)
    case 22 => Orchestration.teleportEntity("myEntity", Vec2F(5f, 5f))
    case _ if (tickCount < 25) =>
    case _ if ((tickCount + 30) % 40 == 0) => Orchestration.moveEntity("myEntity", Vec2F(5f, 5f), Vec2F(15f, 5f))
    case _ if ((tickCount + 20) % 40 == 0) => Orchestration.moveEntity("myEntity", Vec2F(15f, 5f), Vec2F(15f, 15f))
    case _ if ((tickCount + 10) % 40 == 0) => Orchestration.moveEntity("myEntity", Vec2F(15f, 15f), Vec2F(5f, 15f))
    case _ if ((tickCount + 0) % 40 == 0) => Orchestration.moveEntity("myEntity", Vec2F(5f, 15f), Vec2F(5f, 5f))
    case _ =>
  }
}
