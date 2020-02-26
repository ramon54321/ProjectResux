package sux.client

import sux.common.math.{MVec2D, Vec2D}
import sux.common.state.Entity

import scala.collection.mutable
import scala.util.Try

object InterfaceState {
  // Keys
  private val keys: mutable.HashMap[String, Boolean] = mutable.HashMap()
  private val keysUp: mutable.HashMap[String, Boolean] = mutable.HashMap()
  private val keysDown: mutable.HashMap[String, Boolean] = mutable.HashMap()
  def setKeyDown(key: String): Unit = {
    keys(key) = true
    keysDown(key) = true
  }
  def setKeyUp(key: String): Unit = {
    keys(key) = false
    keysUp(key) = true
  }
  def resetKeys(): Unit = {
    keysUp.foreach(keyValue => if (keyValue._2) keysUp(keyValue._1) = false)
    keysDown.foreach(keyValue => if (keyValue._2) keysDown(keyValue._1) = false)
  }
  def getKey(key: String): Boolean = Try(keys(key)).getOrElse(false)
  def getKeyUp(key: String): Boolean = Try(keysUp(key)).getOrElse(false)
  def getKeyDown(key: String): Boolean = Try(keysDown(key)).getOrElse(false)

  // Mouse
  private val mouseCanvasPosition: MVec2D = MVec2D(0, 0)
  def setMouseCanvasPosition(x: Double, y: Double): Unit = {
    mouseCanvasPosition.x = x
    mouseCanvasPosition.y = y
  }
  def getMouseCanvasPosition: MVec2D = mouseCanvasPosition
  private val clicks: mutable.HashMap[String, Boolean] = mutable.HashMap()
  def resetMouse(): Unit = {
    clicks.foreach(keyValue => if (keyValue._2) clicks(keyValue._1) = false)
  }
  def setClickLeft(): Unit = {
    clicks("left") = true
  }
  def setClickRight(): Unit = {
    clicks("right") = true
  }
  def getClickLeft: Boolean = Try(clicks("left")).getOrElse(false)
  def getClickRight: Boolean = Try(clicks("right")).getOrElse(false)

  // Context Menu
  private var isContextMenuOpen: Boolean = false
  def openContextMenu(): Unit = isContextMenuOpen = true
  def closeContextMenu(): Unit = isContextMenuOpen = false
  def getIsContextMenuOpen: Boolean = isContextMenuOpen
  private var contextMenu: Either[String, ContextMenuNode] = Left("No Root Node")
  def getContextMenu: Either[String, ContextMenuNode] = contextMenu
  def setContextMenu(contextMenu: Either[String, ContextMenuNode]): Unit = this.contextMenu = contextMenu
  def clearContextMenu(): Unit = contextMenu = Left("Cleared Root Node")
  private var hoverNode: Option[ContextMenuNode] = None
  def getHoverNode: Option[ContextMenuNode] = hoverNode
  def setHoverNode(node: ContextMenuNode): Unit = hoverNode = Some(node)
  def clearHoverNode(): Unit = hoverNode = None
  private var contextMenuCanvasCenter: Vec2D = Vec2D(0, 0)
  def getContextMenuCanvasCenter: Vec2D = contextMenuCanvasCenter
  def setContextMenuCanvasCenter(canvasCenter: Vec2D): Unit = contextMenuCanvasCenter = canvasCenter

  // Selection
  private var selectedEntity: Option[Entity] = None
  def getSelectedEntity: Option[Entity] = selectedEntity
  def setSelectedEntity(entity: Entity): Unit = selectedEntity = Some(entity)
  def clearSelectedEntity(): Unit = {
    selectedEntity = None
    closeContextMenu()
  }

  // Hover
  private var nearestHoverEntity: Option[Entity] = None
  def getNearestHoverEntity: Option[Entity] = nearestHoverEntity
  def setNearestHoverEntity(entity: Entity): Unit = nearestHoverEntity = Some(entity)
  def clearNearestHoverEntity(): Unit = nearestHoverEntity = None
  private var hoverEntity: Option[Entity] = None
  def getHoverEntity: Option[Entity] = hoverEntity
  def setHoverEntity(entity: Entity): Unit = hoverEntity = Some(entity)
  def clearHoverEntity(): Unit = hoverEntity = None
}
