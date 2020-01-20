package sux.common.math

import scala.math.Pi

object Circle {
  def arcLength(r: Float, angle: Float): Float = {
    ((2 * Pi * r * angle) / (2 * Pi)).toFloat
  }
}
