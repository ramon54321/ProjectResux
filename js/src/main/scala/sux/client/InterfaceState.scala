package sux.client

import sux.common.math.{MVec2D, Vec2D}

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

  // Context Menu
  private var contextMenuNode: Option[ContextMenuNode] = None
  private var contextMenuCanvasCenter: Vec2D = Vec2D(0, 0)
  private var contextMenuHoverNode: Option[ContextMenuNode] = None
  def getContextMenuNode: Option[ContextMenuNode] = contextMenuNode
  def setContextMenuNode(node: ContextMenuNode): Unit = contextMenuNode = Some(node)
  def clearContextMenuNode(): Unit = contextMenuNode = None
  def getContextMenuHoverNode: Option[ContextMenuNode] = contextMenuHoverNode
  def setContextMenuHoverNode(node: ContextMenuNode): Unit = contextMenuHoverNode = Some(node)
  def clearContextMenuHoverNode(): Unit = contextMenuHoverNode = None
  def getContextMenuCanvasCenter: Vec2D = contextMenuCanvasCenter
  def setContextMenuCanvasCenter(canvasCenter: Vec2D): Unit = contextMenuCanvasCenter = canvasCenter
}
