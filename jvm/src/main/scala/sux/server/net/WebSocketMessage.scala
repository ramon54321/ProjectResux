package sux.server.net

import org.java_websocket.WebSocket

case class WebSocketMessage(webSocket: WebSocket, message: String)
