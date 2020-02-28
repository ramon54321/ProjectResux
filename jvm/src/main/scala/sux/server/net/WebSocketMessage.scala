package sux.server.net

import org.java_websocket.WebSocket

case class WebSocketMessage(webSocket: WebSocket, time: Long, message: String)
