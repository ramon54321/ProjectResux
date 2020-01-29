package sux.client.rendering.extensions

import org.scalajs.dom.raw.CanvasRenderingContext2D

import scala.scalajs.js

class ExtendedCanvasRenderingContext2D extends CanvasRenderingContext2D {
  def stroke(path: Path2D): Unit = js.native
  def fill(path: Path2D): Unit = js.native
}
