package sux.server.game.movement

import sux.common.math.{DVec2F, Vec2F}
import sux.common.state.Entity
import sux.server.game.Specs
import sux.server.game.Specs.Human

object Movement {
  def getPath(entity: Entity, from: Vec2F, to: Vec2F, startTime: Long): Option[DVec2F] = {
    val spec = Specs.fromTag(entity.specTag)
    spec match {
      case human: Human =>
        val speed = human.walkSpeed
        val duration = ((Vec2F.distance(from, to) / speed) * 1000).toLong
        Some(DVec2F(startTime -> from, startTime + duration -> to))
      case _ => None
    }
  }
}
