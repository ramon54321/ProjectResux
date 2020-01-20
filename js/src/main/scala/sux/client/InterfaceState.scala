package sux.client

import scala.collection.mutable
import scala.util.Try

object InterfaceState {
  private val keys: mutable.HashMap[String, Boolean] = mutable.HashMap()
  def setKeyDown(key: String): Unit = keys(key) = true
  def setKeyUp(key: String): Unit = keys(key) = false
  def getKey(key: String): Boolean = Try(keys(key)).getOrElse(false)
}
