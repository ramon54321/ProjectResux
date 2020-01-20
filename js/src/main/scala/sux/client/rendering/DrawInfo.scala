package sux.client.rendering

import org.scalajs.dom.CanvasRenderingContext2D
import sux.common.math.{MutableRectV2F, MutableVector2D}

case class DrawInfo(context: CanvasRenderingContext2D,
                    canvasSize: MutableVector2D,
                    screenRect: MutableRectV2F,
                    var camera: Camera)