package sux.server.game

import org.java_websocket.WebSocket
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.{ClientActions, WorldActions}
import sux.common.math.Vec2F
import sux.server.Hub

object ClientActionHandler {
  def handleClientAction(clientAction: ClientAction, time: Long, webSocket: WebSocket): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => Hub.dispatchTo(WorldActions.Ping(timestamp), webSocket)
    case ClientActions.FullStateUpdate() => Hub.dispatchFullStateTo(webSocket)
    case ClientActions.MoveEntity(entityId, position, moveSpeed) => Orchestration.moveEntity(entityId, Vec2F.fromSerializable(position), moveSpeed)
    case ClientActions.StopEntity(entityId) => Orchestration.stopEntity(entityId)
    case _ => println(s"[CRITICAL] Unknown ClientAction Received - $clientAction")
  }
}
