package sux.common.math

import scala.math.{round, floor, ceil}

object Spread {
  def nearest(spread: Int, value: Float): Float = round(value / spread) * spread
  def floored(spread: Int, value: Float): Float = (floor(value / spread) * spread).toFloat
  def ceiled(spread: Int, value: Float): Float = (ceil(value / spread) * spread).toFloat
}
