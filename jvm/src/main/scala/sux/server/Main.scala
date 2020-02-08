package sux.server

import org.java_websocket.WebSocket
import sux.common.actions.ClientActions
import sux.common.actions.WorldActions
import sux.common.actions.ClientActions.ClientAction
import sux.common.actions.WorldActions.WorldAction
import sux.common.math.{DeterministicVector2F, LUTF}
import sux.common.state.WorldState
import sux.server.net.{WebSocketMessage, WebSocketServer}

import scala.collection.mutable

object Hub {
  val worldState = new WorldState
  val worldActionHistory = new mutable.ListBuffer[WorldAction]
  val webSocketMessageQueue = new mutable.Queue[WebSocketMessage]
  var webSocketServer: WebSocketServer = _

  def patchWorldState(worldAction: WorldAction): Unit = {
    worldActionHistory.append(worldAction)
    worldState.patch(worldAction)
    val worldActionJson = WorldActions.Serializer.toJson(worldAction)
    println(f"[DISPATCH] ${worldActionJson}")
    webSocketServer.broadcast(worldActionJson)
  }

  def handleClientAction(clientAction: ClientAction, webSocket: WebSocket): Unit = clientAction match {
    case ClientActions.Ping(timestamp) => {
      patchWorldState(WorldActions.Ping(timestamp))
    }
    case ClientActions.FullStateUpdate() => worldActionHistory.foreach(worldAction => webSocket.send(WorldActions.Serializer.toJson(worldAction)))
  }
}

object Main extends App {
  def debugAction(tickCount: Int): Unit = tickCount match {
    case 10 => {
      Hub.patchWorldState(WorldActions.SpawnEntity("e1", "man", {
        val now = System.currentTimeMillis()
        DeterministicVector2F(LUTF(now -> 0f, now + 10000L -> 20f), LUTF(now -> 0f, now + 15000L -> 10f)).toSerializable
      }))
      Hub.patchWorldState(WorldActions.SetEntityAttributeString("e1", "Name", "Alan P. Wilson"))
    }
    case 15 => Hub.patchWorldState(WorldActions.SetEntityAttributeFloat("e1", "Health", 100f))
    case 25 => Hub.patchWorldState(WorldActions.SetEntityPosition("e1", {
      val now = System.currentTimeMillis()
      DeterministicVector2F(LUTF(now -> 20f, now + 10000L -> 10f), LUTF(now -> 5f, now + 15000L -> 15f)).toSerializable
    }))
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
              .map(Hub.handleClientAction(_, webSocketMessage.webSocket))
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


