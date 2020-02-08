package sux.client.rendering.layers

import sux.client.debug.Timer
import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.Circle

class EntitiesRenderLayer extends RenderLayer {

  private val timer = new Timer("Entities")

  override def draw(drawInfo: DrawInfo) {
    timer.markStart()

    val now = System.currentTimeMillis()

    drawInfo.context.fillStyle = "rgba(255, 255, 255, 1.0)"
    drawInfo.worldState.entities.foreach(entity => Circle.drawCircleFill(drawInfo, entity.position.lookup(now), 3f))

    timer.markEnd()
  }
}
