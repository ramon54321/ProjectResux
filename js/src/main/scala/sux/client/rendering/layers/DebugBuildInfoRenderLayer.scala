package sux.client.rendering.layers

import sux.client.Config
import sux.client.rendering.DrawInfo
import sux.client.rendering.drawing.Text.FontStyle
import sux.client.rendering.drawing.Text
import sux.common.math.Vector2D

class DebugBuildInfoRenderLayer extends RenderLayer {

  private val styleFont = "rgba(255, 255, 255, 0.8)"

  override def draw(drawInfo: DrawInfo): Unit = {
    drawInfo.context.fillStyle = styleFont
    Text.setFont(drawInfo, FontStyle.STATIC_TINY)
    Text.drawTextCanvasSpace(drawInfo, Vector2D(15.0, 25.0), f"${Config.label} v${Config.version} - ${Config.commitHash} - ${Config.commitAuthor}")
    Text.drawTextCanvasSpace(drawInfo, Vector2D(15.0, 40.0), f"Build: ${Config.clientBuildHashShort}")
  }
}