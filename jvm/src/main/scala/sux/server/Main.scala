package sux.server

import sux.common.actions.ClientActions
import sux.common.actions.WorldActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState
import sux.server.net.WebSocketServer

object Main extends App {

  def patchWorldStateWithWorldActionAndDispatch(webSocketServer: WebSocketServer, worldState: WorldState, worldAction: WorldAction): Unit = {
    worldState.patch(worldAction)
    webSocketServer.broadcast(WorldActions.Serializer.toJson(worldAction))
  }

  def handleClientAction(webSocketServer: WebSocketServer, worldState: WorldState, clientAction: ClientAction): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => {
      patchWorldStateWithWorldActionAndDispatch(webSocketServer, worldState, WorldActions.Ping(timestamp))
    }
  }

  override def main(args: Array[String]): Unit = {

    val worldState = new WorldState

    var webSocketServer: WebSocketServer = null
    val socketServerThread = new Thread {
      override def run() {
        webSocketServer = new WebSocketServer(8081, webSocketMessage => {
          val clientAction = ClientActions.Serializer.fromJson(webSocketMessage.message).get
          handleClientAction(webSocketServer, worldState, clientAction)
        })
      }
    }
    socketServerThread.start()

    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run() {
        println("Shutting down WebSocketServer")
        webSocketServer.stop()
      }
    })
  }
}


