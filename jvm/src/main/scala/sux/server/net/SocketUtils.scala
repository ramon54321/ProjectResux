package sux.server.net

import org.java_websocket.WebSocket

import scala.util.Try

object SocketUtils {
  def getSocketId(webSocket: WebSocket): Option[String] =  Try(webSocket.getRemoteSocketAddress)
    .map(_.toString).toOption
}
