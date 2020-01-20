package sux.server.net

import org.java_websocket.WebSocket

object SocketUtils {
  def getSocketId(webSocket: WebSocket): String = webSocket.getRemoteSocketAddress.toString
}
