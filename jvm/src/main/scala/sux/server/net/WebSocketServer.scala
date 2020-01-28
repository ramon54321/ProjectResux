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
    SocketUtils.getSocketId(webSocket).map(sockets.put(_, webSocket))
  }

  override def onClose(webSocket: WebSocket, code: Int, reason: String, remote: Boolean): Unit = {
    println("[WSS] Close")
  }

  override def onClosing(webSocket: WebSocket, code: Int, reason: String, remote: Boolean): Unit = {
    println("[WSS] Closing")
    SocketUtils.getSocketId(webSocket).map(sockets.remove)
  }

  override def onMessage(webSocket: WebSocket, message: String): Unit = {
    println(s"[WSS] ${SocketUtils.getSocketId(webSocket).getOrElse("Unknown")}: $message")
    onMessage(WebSocketMessage(webSocket, message))
  }

  override def onError(webSocket: WebSocket, ex: Exception): Unit = {
    stop()
    println("[WSS] Error")
    ex.printStackTrace()
  }

  override def onStart(): Unit = println("[WSS] Start")

  def socketsString(): String = sockets.map(a => f"${a._1}").mkString("\n")
}
