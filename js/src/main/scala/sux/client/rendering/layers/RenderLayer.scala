package sux.client.rendering.layers

import sux.client.rendering.FrameInfo

abstract class RenderLayer {
  def draw(frameInfo: FrameInfo): Unit
  def onCanvasResize(frameInfo: FrameInfo): Unit = {}
}
