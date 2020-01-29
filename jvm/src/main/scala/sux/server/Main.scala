package sux.server

import sux.common.actions.ClientActions
import sux.common.actions.WorldActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState
import sux.server.net.WebSocketServer

import scala.collection.mutable
import util.control.Breaks._

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
    /**
     * Initialize Data Structures
     */
    val worldState = new WorldState
    val clientActionQueue = new mutable.Queue[ClientAction]

    /**
     * WebSocket Connection
     */
    var webSocketServer: WebSocketServer = null
    webSocketServer = new WebSocketServer(11101, webSocketMessage => {
      ClientActions.Serializer.fromJson(webSocketMessage.message)
        .map(clientActionQueue.enqueue(_))
    })

    /**
     * Client Action Processing
     */
    new Thread {
      override def run(): Unit = {
        while(true) {
          breakable {
            if (clientActionQueue.isEmpty) {
              Thread.sleep(1000)
              break
            }
            handleClientAction(webSocketServer, worldState, clientActionQueue.dequeue())
          }
        }
      }
    }.start()

    /**
     * Shutdown
     */
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run() {
        println("[MAIN] Shutting down WebSocketServer")
        webSocketServer.stop()
      }
    })
  }
}


