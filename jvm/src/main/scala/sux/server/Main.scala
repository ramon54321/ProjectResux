package sux.server

import org.java_websocket.WebSocket
import sux.common.actions.ClientActions
import sux.common.actions.WorldActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.WorldState
import sux.server.net.{WebSocketMessage, WebSocketServer}

import scala.collection.mutable

object Hub {
  val worldState = new WorldState
  val worldActionHistory = new mutable.ListBuffer[WorldAction]
  val webSocketMessageQueue = new mutable.Queue[WebSocketMessage]
  var webSocketServer: WebSocketServer = _
}

object Main extends App {
  def patchWorldStateWithWorldActionAndDispatch(worldAction: WorldAction): Unit = {
    Hub.worldActionHistory.append(worldAction)
    Hub.worldState.patch(worldAction)
    Hub.webSocketServer.broadcast(WorldActions.Serializer.toJson(worldAction))
  }

  def handleClientAction(clientAction: ClientAction, webSocket: WebSocket): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => {
      patchWorldStateWithWorldActionAndDispatch(WorldActions.Ping(timestamp))
    }
    case ClientActions.FullStateUpdate() => Hub.worldActionHistory.foreach(worldAction => webSocket.send(WorldActions.Serializer.toJson(worldAction)))
  }

  def debugAction(tickCount: Int): Unit = tickCount match {
    case 10 => patchWorldStateWithWorldActionAndDispatch(WorldActions.Spawn("e1", "man"))
    case _ =>
  }

  override def main(args: Array[String]): Unit = {
    /**
     * WebSocket Connection
     */
    Hub.webSocketServer = new WebSocketServer(11101, webSocketMessage => Hub.webSocketMessageQueue.enqueue(webSocketMessage))

    /**
     * Client Action Processing
     */
    var tickCount: Int = 0
    new Thread {
      override def run(): Unit = {
        while(true) {
          println(f"[TICK] ${tickCount}")
          while (Hub.webSocketMessageQueue.nonEmpty) {
            val webSocketMessage = Hub.webSocketMessageQueue.dequeue()
            ClientActions.Serializer.fromJson(webSocketMessage.message)
              .map(handleClientAction(_, webSocketMessage.webSocket))
          }
          debugAction(tickCount)
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
        Hub.webSocketServer.stop()
      }
    })
  }
}


