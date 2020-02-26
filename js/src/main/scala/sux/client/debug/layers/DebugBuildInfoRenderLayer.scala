package sux.client.debug.layers

import sux.client.Config
import sux.client.rendering.FrameInfo
import sux.client.rendering.drawing.Text
import sux.client.rendering.drawing.Text.FontStyle
import sux.client.rendering.layers.RenderLayer
import sux.common.math.Vec2D

class DebugBuildInfoRenderLayer extends RenderLayer {

  private val styleFont = "rgba(255, 255, 255, 0.8)"

  override def draw(frameInfo: FrameInfo): Unit = {
    frameInfo.context.fillStyle = styleFont
    Text.setFont(frameInfo, FontStyle.STATIC_TINY)
    Text.drawTextCanvasSpace(frameInfo, Vec2D(15.0, 25.0), f"Version: ${Config.version} @ ${Config.commitHash} by ${Config.commitAuthor}")
    Text.drawTextCanvasSpace(frameInfo, Vec2D(15.0, 40.0), f"Build: ${Config.clientBuildHashShort} - ${Config.buildDate}")
    Text.drawTextCanvasSpace(frameInfo, Vec2D(15.0, 55.0), f"Config: ${Config.label}")
  }
}
