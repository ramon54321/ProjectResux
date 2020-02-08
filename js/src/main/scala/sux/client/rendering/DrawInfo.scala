package sux.client.rendering

import sux.client.rendering.extensions.ExtendedCanvasRenderingContext2D
import sux.common.math.{MutableRectV2F, MutableVector2D}
import sux.common.state.WorldState

case class DrawInfo(context: ExtendedCanvasRenderingContext2D,
                    canvasSize: MutableVector2D,
                    screenWorldRect: MutableRectV2F,
                    worldState: WorldState,
                    var camera: Camera)