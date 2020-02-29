package sux.client

import org.scalajs.dom.raw.WebSocket
import sux.client.rendering.{Camera, CanvasRenderer}
import sux.common.actions.{ClientActions, WorldActions}

object Main {

  def main(args: Array[String]): Unit = {
    println(f"[Main] Using ${Config.label} config")

    /**
     * Connect to Server
     */
    val serverAddress = f"ws://${Config.serverHost}:${Config.serverPort}"
    println(s"[Main] Connecting to server at ${serverAddress}")
    Hub.webSocket = new WebSocket(serverAddress)
    Hub.webSocket.onerror = _ => println("[WS] Error")
    Hub.webSocket.onopen = _ => {
      println(f"[WS] Connected to server at ${serverAddress}")
      Hub.dispatch(ClientActions.FullStateUpdate())
      val now = System.currentTimeMillis()
      Hub.worldState.startTimeSync(now)
      Hub.dispatch(ClientActions.Sync(now))
    }
    Hub.webSocket.onclose = _ => println("[WS] Closed connection")
    Hub.webSocket.onmessage = event =>
      WorldActions.Serializer.fromJson(event.data.asInstanceOf[String])
        .map(Hub.handleWorldAction)

    /**
     * Setup Renderer
     */
    val renderer = new CanvasRenderer(Hub.worldState, new Camera())
  }
}
