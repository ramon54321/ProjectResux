package sux.server

import sux.common.actions.ClientActions
import sux.server.game.{ClientActionHandler, TickHandler}
import sux.server.net.WebSocketServer

object Main extends App {
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
              .map(ClientActionHandler.handleClientAction(_, webSocketMessage.time, webSocketMessage.webSocket))
          }
          TickHandler.tick(tickCount)
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
