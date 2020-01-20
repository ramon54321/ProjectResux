package sux.client.rendering.layers

import sux.client.rendering.DrawInfo

abstract class RenderLayer {
  def draw(drawInfo: DrawInfo): Unit
  def onCanvasResize(drawInfo: DrawInfo): Unit = {}
}
