package sux.client.rendering

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLCanvasElement
import sux.client.rendering.extensions.{ExtendedCanvasRenderingContext2D, Path2D}

class Sprite(val width: Int, val height: Int) {
  val canvas: HTMLCanvasElement = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
  val context: ExtendedCanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[ExtendedCanvasRenderingContext2D]
  canvas.width = width
  canvas.height = height

  context.stroke(new Path2D(f"M 0 0 L ${width} 0 L ${width} ${height} L 0 ${height} z M 0 0 L ${width} ${height}"))
}
