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

  def interpolate(lowerKey: Long, upperKey: Long, lowerValue: Float, upperValue: Float, key: Long): Float = {
    val t = Interpolation.inverseLerp(lowerKey, upperKey, key)
    if (lowerValue == upperValue) lowerValue else lerp(lowerValue, upperValue, t)
  }
}
