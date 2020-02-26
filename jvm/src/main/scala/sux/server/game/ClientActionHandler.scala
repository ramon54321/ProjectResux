package sux.server.game

import org.java_websocket.WebSocket
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.{ClientActions, WorldActions}
import sux.server.Hub

object ClientActionHandler {
  def handleClientAction(clientAction: ClientAction, webSocket: WebSocket): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => Hub.dispatchTo(WorldActions.Ping(timestamp), webSocket)
    case ClientActions.FullStateUpdate() => Hub.worldActionHistory.foreach(worldAction => Hub.dispatchTo(worldAction, webSocket))
    case _ => println(s"[CRITICAL] Unknown ClientAction Received - $clientAction")
  }
}
