package sux.server

import org.java_websocket.WebSocket
import sux.common.actions.ClientActions
import sux.common.actions.WorldActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState
import sux.server.net.{WebSocketMessage, WebSocketServer}

import scala.collection.mutable

object Main extends App {

  def patchWorldStateWithWorldActionAndDispatch(webSocketServer: WebSocketServer, worldState: WorldState, worldAction: WorldAction, worldActionHistory: mutable.ListBuffer[WorldAction]): Unit = {
    worldActionHistory.append(worldAction)
    worldState.patch(worldAction)
    webSocketServer.broadcast(WorldActions.Serializer.toJson(worldAction))
  }

  def handleClientAction(webSocketServer: WebSocketServer, worldState: WorldState, clientAction: ClientAction, webSocket: WebSocket, worldActionHistory: mutable.ListBuffer[WorldAction]): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => {
      patchWorldStateWithWorldActionAndDispatch(webSocketServer, worldState, WorldActions.Ping(timestamp), worldActionHistory)
    }
    case ClientActions.FullStateUpdate() => worldActionHistory.foreach(worldAction => webSocket.send(WorldActions.Serializer.toJson(worldAction)))
  }

  def debugAction(tickCount: Int, webSocketServer: WebSocketServer, worldState: WorldState, worldActionHistory: mutable.ListBuffer[WorldAction]): Unit = tickCount match {
    case 10 => patchWorldStateWithWorldActionAndDispatch(webSocketServer, worldState, WorldActions.Spawn("e1", "man"), worldActionHistory)
    case _ =>
  }

  override def main(args: Array[String]): Unit = {
    /**
     * Initialize Data Structures
     */
    val worldState = new WorldState
    val worldActionHistory = new mutable.ListBuffer[WorldAction]
    val webSocketMessageQueue = new mutable.Queue[WebSocketMessage]

    /**
     * WebSocket Connection
     */
    var webSocketServer: WebSocketServer = null
    webSocketServer = new WebSocketServer(11101, webSocketMessage => {
      webSocketMessageQueue.enqueue(webSocketMessage)
    })

    /**
     * Client Action Processing
     */
    var tickCount: Int = 0
    new Thread {
      override def run(): Unit = {
        while(true) {
          println(f"[TICK] ${tickCount}")
          while (webSocketMessageQueue.nonEmpty) {
            val webSocketMessage = webSocketMessageQueue.dequeue()
            ClientActions.Serializer.fromJson(webSocketMessage.message)
              .map(handleClientAction(webSocketServer, worldState, _, webSocketMessage.webSocket, worldActionHistory))
          }
          debugAction(tickCount, webSocketServer, worldState, worldActionHistory)
          Thread.sleep(1000)
          tickCount += 1
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


