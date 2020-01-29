package sux.client.rendering

import sux.client.rendering.extensions.ExtendedCanvasRenderingContext2D
import sux.common.math.{MutableRectV2F, MutableVector2D}

case class DrawInfo(context: ExtendedCanvasRenderingContext2D,
                    canvasSize: MutableVector2D,
                    screenRect: MutableRectV2F,
                    var camera: Camera)