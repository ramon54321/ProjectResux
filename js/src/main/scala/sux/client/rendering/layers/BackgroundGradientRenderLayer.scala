package sux.client.rendering.layers

import org.scalajs.dom.CanvasGradient
import sux.client.rendering.DrawInfo

class BackgroundGradientRenderLayer(drawInfo: DrawInfo) extends RenderLayer {

  private var gradient: CanvasGradient = getGradient(drawInfo)

  private def getGradient(drawInfo: DrawInfo): CanvasGradient = {
    val gradient = drawInfo.context.createRadialGradient(drawInfo.canvasSize.x / 2.0, drawInfo.canvasSize.y / 2.0, 0.0, drawInfo.canvasSize.x / 2.0, drawInfo.canvasSize.y / 2.0, drawInfo.canvasSize.y)
    gradient.addColorStop(0.0, "rgba(20, 90, 160, 0.85)")
    gradient.addColorStop(1.0, "rgba(20, 90, 160, 1.00)")
    gradient
  }

  override def draw(drawInfo: DrawInfo): Unit = {
    drawInfo.context.fillStyle = gradient
    drawInfo.context.fillRect(0.0, 0.0, drawInfo.canvasSize.x, drawInfo.canvasSize.y)
  }

  override def onCanvasResize(drawInfo: DrawInfo): Unit = {
    gradient = getGradient(drawInfo)
  }
}
