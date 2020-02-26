package sux.client.rendering.layers

import sux.client.InterfaceState
import sux.client.debug.Timer
import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.{Circle, Text}
import sux.common.math.Vec2D

class EntitiesRenderLayer extends RenderLayer {

  private val timer = new Timer("Entities")

  override def draw(frameInfo: FrameInfo) {
    timer.markStart()

    frameInfo.context.fillStyle = "rgba(255, 255, 255, 1.0)"
    frameInfo.context.strokeStyle = "rgba(255, 255, 255, 1.0)"
    frameInfo.context.lineWidth = 2.0
    frameInfo.worldState.entities.foreach(entity => {
      val position = entity.position.lookup(frameInfo.worldTime)
      Circle.drawCircleFill(frameInfo, position, 3f)
      entity.attributes.zipWithIndex.foreach(zip => {
        val key = zip._1._1
        val value = zip._1._2
        val index = zip._2
        Text.drawText(frameInfo, position, Vec2D(10.0, index * 14.0), f"${key}: ${value}")
      })
    })
    InterfaceState.getSelectedEntity.foreach(entity => Circle.drawCircle(frameInfo, entity.position.lookup(frameInfo.worldTime), 10f))

    timer.markEnd()
  }
}
