package sux.client

import org.scalajs.dom.raw.WebSocket
import scala.scalajs.js.Date
import sux.client.rendering.{Camera, CanvasRenderer}
import sux.common.actions.{ClientActions, WorldActions}
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState

object Main {

  def handleWorldAction(worldState: WorldState, worldAction: WorldAction): Unit = {
    worldState.patch(worldAction)
  }

  def main(args: Array[String]): Unit = {

    println(f"[Main] Using ${Config.label} config")

    /**
     * Initialize Data Structures
     */
    val worldState = new WorldState

    /**
     * Connect to Server
     */
    val serverAddress = f"ws://${Config.serverHost}:${Config.serverPort}"
    println(s"[Main] Connecting to server at ${serverAddress}")
    val webSocket = new WebSocket(serverAddress)
    webSocket.onerror = _ => println("[WS] Error")
    webSocket.onopen = _ => {
      println(f"[WS] Connected to server at ${serverAddress}")
      webSocket.send(ClientActions.Serializer.toJson(ClientActions.Ping(Date.now().toLong)))
    }
    webSocket.onclose = _ => println("[WS] Closed connection")
    webSocket.onmessage = event =>
      WorldActions.Serializer.fromJson(event.data.asInstanceOf[String])
        .map(handleWorldAction(worldState, _))

    /**
     * Setup Renderer
     */
    val renderer = new CanvasRenderer(worldState, new Camera())
  }
}
