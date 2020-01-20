package sux.common.math

object Interpolation {
  def inverseLerp(a: Float, b: Float, x: Float): Float = {
    (x - a) / (b - a)
  }

  def inverseLerp(a: Long, b: Long, x: Long): Float = {
    val numerator = (x - a).toFloat
    val denominator = (b - a).toFloat
    if (denominator == 0f) 0f else numerator / denominator
  }

  def lerp(a: Float, b: Float, t: Float): Float = {
    a * (1 - t) + b * t
  }

}
