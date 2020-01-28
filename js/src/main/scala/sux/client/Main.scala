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
    println("Starting websocket")
//    val webSocket = new WebSocket("ws://104.248.21.234:11101")
    val webSocket = new WebSocket("ws://127.0.0.1:11101")
    webSocket.onerror = err => println(err)
    webSocket.onopen = event => println("Open " + event)
    webSocket.onclose = event => println("Close " + event)
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
