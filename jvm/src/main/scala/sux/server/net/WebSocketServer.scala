package sux.server.net

import java.net.InetSocketAddress

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.{WebSocketServer => JavaWebSocketServer}

import scala.collection.mutable

class WebSocketServer(port: Int, onMessage: WebSocketMessage => Unit) extends JavaWebSocketServer(new InetSocketAddress(port)) {
  private val sockets: mutable.HashMap[String, WebSocket] = new mutable.HashMap()

  start()

  override def onOpen(webSocket: WebSocket, handshake: ClientHandshake): Unit = {
    println("WSS: Open")
    sockets.put(SocketUtils.getSocketId(webSocket), webSocket)
  }

  override def onClose(webSocket: WebSocket, code: Int, reason: String, remote: Boolean): Unit = {
    println("WSS: Close")
    sockets.remove(SocketUtils.getSocketId(webSocket))
  }

  override def onMessage(webSocket: WebSocket, message: String): Unit = {
    println(s"${SocketUtils.getSocketId(webSocket)}: $message")
    onMessage(WebSocketMessage(webSocket, message))
  }

  override def onError(webSocket: WebSocket, ex: Exception): Unit = {
    stop()
    println("WSS: Error")
    ex.printStackTrace()
  }

  override def onStart(): Unit = println("WSS: Start")
}
