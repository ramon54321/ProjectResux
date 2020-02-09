package sux.client.rendering.layers

import sux.client.debug.Timer
import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.Vec2D

class EntitiesRenderLayer extends RenderLayer {

  private val timer = new Timer("Entities")

  override def draw(drawInfo: DrawInfo) {
    timer.markStart()

    val now = System.currentTimeMillis()

    drawInfo.context.fillStyle = "rgba(255, 255, 255, 1.0)"
    drawInfo.worldState.entities.foreach(entity => {
      val position = entity.position.lookup(now)
      Circle.drawCircleFill(drawInfo, position, 3f)
      entity.attributes.zipWithIndex.foreach(zip => {
        val key = zip._1._1
        val value = zip._1._2
        val index = zip._2
        Text.drawText(drawInfo, position, Vec2D(10.0, index * 14.0), f"${key}: ${value}")
      })
    })

    timer.markEnd()
  }
}
