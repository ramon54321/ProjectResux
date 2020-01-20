package sux.client

import org.scalajs.dom.raw.WebSocket
import sux.client.rendering.{Camera, CanvasRenderer}
import sux.common.actions.{ClientActions, WorldActions}
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState

import scala.scalajs.js.Date

object Main {

  def handleWorldAction(worldState: WorldState, worldAction: WorldAction): Unit = {
    worldState.patch(worldAction)
  }

  def main(args: Array[String]): Unit = {

    /**
     * Initialize Data Structures
     */
    val worldState = new WorldState

    /**
     * Connect to Server
     */
    val webSocket = new WebSocket("ws://localhost:8081")
    webSocket.onmessage = event => {
      handleWorldAction(worldState, WorldActions.Serializer.fromJson(event.data.asInstanceOf[String]).get)
    }

    /**
     * Setup Renderer
     */
    val renderer = new CanvasRenderer(worldState, new Camera())

    webSocket.onopen = event => webSocket.send(ClientActions.Serializer.toJson(ClientActions.Ping(Date.now().toLong)))
  }
}
