package sux.server.game

import sux.common.math.Vector2F

object TickHandler {
  def tick(tickCount: Int): Unit = tickCount match {
    case 10 => Orchestration.spawnEntity("myEntity")
    case 15 => Orchestration.damageEntity("myEntity", 21)
    case 22 => Orchestration.teleportEntity("myEntity", Vector2F(5f, 5f))
    case _ =>
  }
}
