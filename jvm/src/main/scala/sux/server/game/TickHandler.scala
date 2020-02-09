package sux.server.game

import sux.common.math.Vec2F

object TickHandler {
  def tick(tickCount: Int): Unit = tickCount match {
    case 5 =>
      Orchestration.spawnEntity("e1")
      Orchestration.spawnEntity("e2")
      Orchestration.teleportEntity("e1", Vec2F(5f, 5f))
      Orchestration.teleportEntity("e2", Vec2F(15f, 10f))
    case 15 => Orchestration.shootEntity("e1", "e2")
    case _ if (tickCount < 25) =>
    case _ if ((tickCount + 30) % 40 == 0) => Orchestration.moveEntity("e1", Vec2F(5f, 5f), Vec2F(15f, 5f))
    case _ if ((tickCount + 20) % 40 == 0) => Orchestration.moveEntity("e1", Vec2F(15f, 5f), Vec2F(15f, 15f))
    case _ if ((tickCount + 10) % 40 == 0) => Orchestration.moveEntity("e1", Vec2F(15f, 15f), Vec2F(5f, 15f))
    case _ if ((tickCount + 0) % 40 == 0) => Orchestration.moveEntity("e1", Vec2F(5f, 15f), Vec2F(5f, 5f))
    case _ =>
  }
}
