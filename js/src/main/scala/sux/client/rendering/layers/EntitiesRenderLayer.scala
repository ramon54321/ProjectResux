package sux.client.rendering.layers

import sux.client.InterfaceState
import sux.client.debug.Timer
import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.Vec2D

class EntitiesRenderLayer extends RenderLayer {

  private val timer = new Timer("Entities")

  override def draw(drawInfo: DrawInfo) {
    timer.markStart()

    drawInfo.context.fillStyle = "rgba(255, 255, 255, 1.0)"
    drawInfo.context.strokeStyle = "rgba(255, 255, 255, 1.0)"
    drawInfo.context.lineWidth = 2.0
    drawInfo.worldState.entities.foreach(entity => {
      val position = entity.position.lookup(drawInfo.worldTime)
      Circle.drawCircleFill(drawInfo, position, 3f)
      entity.attributes.zipWithIndex.foreach(zip => {
        val key = zip._1._1
        val value = zip._1._2
        val index = zip._2
        Text.drawText(drawInfo, position, Vec2D(10.0, index * 14.0), f"${key}: ${value}")
      })
    })
    InterfaceState.getSelectedEntity.foreach(entity => Circle.drawCircle(drawInfo, entity.position.lookup(drawInfo.worldTime), 10f))

    timer.markEnd()
  }
}
