package sux.client.rendering

import sux.client.rendering.extensions.ExtendedCanvasRenderingContext2D
import sux.common.math.{MVec2D, MutableRectV2F, Vec2D, Vec2F}
import sux.common.state.WorldState

case class FrameInfo(context: ExtendedCanvasRenderingContext2D,
                     canvasSize: MVec2D,
                     screenWorldRect: MutableRectV2F,
                     worldState: WorldState,
                     var worldTime: Long,
                     var camera: Camera,
                     var mouseCanvasPosition: Vec2D,
                     var mouseWorldPosition: Vec2F,
)