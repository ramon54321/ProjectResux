package sux.server.game

import sux.common.actions.WorldActions
import sux.common.math.{DeterministicVector2F, LUTF}
import sux.server.Hub

object TickHandler {
  def tick(tickCount: Int): Unit = tickCount match {
    case 10 => {
      Hub.patchWorldState(WorldActions.SpawnEntity("e1", "man", {
        val now = System.currentTimeMillis()
        DeterministicVector2F(LUTF(now -> 0f, now + 10000L -> 20f), LUTF(now -> 0f, now + 15000L -> 10f)).toSerializable
      }))
      Hub.patchWorldState(WorldActions.SetEntityAttributeString("e1", "Name", "Alan P. Wilson"))
    }
    case 15 => Hub.patchWorldState(WorldActions.SetEntityAttributeFloat("e1", "Health", 100f))
    case 25 => Hub.patchWorldState(WorldActions.SetEntityPosition("e1", {
      val now = System.currentTimeMillis()
      DeterministicVector2F(LUTF(now -> 20f, now + 10000L -> 10f), LUTF(now -> 5f, now + 15000L -> 15f)).toSerializable
    }))
    case _ =>
  }
}
