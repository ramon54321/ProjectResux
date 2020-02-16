package sux.client

import org.scalajs.dom.raw.WebSocket
import sux.common.actions.ClientActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState

object Hub {
  val worldState = new WorldState
  var webSocket: WebSocket = _

  def handleWorldAction(worldAction: WorldAction): Unit = {
    worldState.patch(worldAction)
  }

  def dispatch(clientAction: ClientAction): Unit = {
    webSocket.send(ClientActions.Serializer.toJson(clientAction))
  }
}
