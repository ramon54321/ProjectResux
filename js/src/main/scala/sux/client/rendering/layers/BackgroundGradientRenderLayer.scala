package sux.client.rendering.layers

import org.scalajs.dom.CanvasGradient
import sux.client.rendering.FrameInfo

class BackgroundGradientRenderLayer(frameInfo: FrameInfo) extends RenderLayer {

  private var gradient: CanvasGradient = getGradient(frameInfo)

  private def getGradient(frameInfo: FrameInfo): CanvasGradient = {
    val gradient = frameInfo.context.createRadialGradient(frameInfo.canvasSize.x / 2.0, frameInfo.canvasSize.y / 2.0, 0.0, frameInfo.canvasSize.x / 2.0, frameInfo.canvasSize.y / 2.0, frameInfo.canvasSize.y)
    gradient.addColorStop(0.0, "rgba(20, 90, 160, 0.85)")
    gradient.addColorStop(1.0, "rgba(20, 90, 160, 1.00)")
    gradient
  }

  override def draw(frameInfo: FrameInfo): Unit = {
    frameInfo.context.fillStyle = gradient
    frameInfo.context.fillRect(0.0, 0.0, frameInfo.canvasSize.x, frameInfo.canvasSize.y)
  }

  override def onCanvasResize(frameInfo: FrameInfo): Unit = {
    gradient = getGradient(frameInfo)
  }
}
