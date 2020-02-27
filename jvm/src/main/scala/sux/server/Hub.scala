package sux.server

import org.java_websocket.WebSocket
import sux.common.actions.WorldActions
import sux.common.actions.WorldActions.WorldAction
import sux.common.state.{Human, Item, WorldState}
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
    dispatchBroadcast(worldAction)
  }

  def dispatchBroadcast(worldAction: WorldAction): Unit = {
    val worldActionJson = WorldActions.Serializer.toJson(worldAction)
    println(f"[DISPATCHING] ${worldActionJson}")
    webSocketServer.broadcast(worldActionJson)
  }

  def dispatchTo(worldAction: WorldAction, webSocket: WebSocket): Unit = {
    val worldActionJson = WorldActions.Serializer.toJson(worldAction)
    println(f"[DISPATCHING SINGLE] ${worldActionJson}")
    webSocket.send(worldActionJson)
  }

  def dispatchFullStateTo(webSocket: WebSocket): Unit = {
    val worldActions = worldState.entities.flatMap(entity => {
      val specActions = entity match {
        case human: Human => Seq(
          WorldActions.SpawnHuman(entity.id, entity.specTag, entity.position.toSerializable)
        ) ++ human.items.map(item => WorldActions.AddEntityItem(entity.id, item))
        case item: Item => Seq(
          WorldActions.SpawnHuman(entity.id, entity.specTag, entity.position.toSerializable)
        )
      }
      val commonActions = entity.attributes.map(attribute => attribute._2 match {
        case a: String => WorldActions.SetEntityAttributeString(entity.id, attribute._1, a)
        case a: Int => WorldActions.SetEntityAttributeInt(entity.id, attribute._1, a)
        case a: Float => WorldActions.SetEntityAttributeFloat(entity.id, attribute._1, a)
      })
      specActions ++ commonActions
    })
    worldActions.foreach(worldAction => dispatchTo(worldAction, webSocket))
  }
}
