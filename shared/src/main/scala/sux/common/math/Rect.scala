package sux.common.math

case class RectF(x: Float, y: Float, w: Float, h: Float)
case class MutableRectF(var x: Float, var y: Float, var w: Float, var h: Float)

case class RectD(x: Double, y: Double, w: Double, h: Double)
case class MutableRectD(var x: Double, var y: Double, var w: Double, var h: Double)

case class RectV2F(topLeft: Vector2F, bottomRight: Vector2F)
case class MutableRectV2F(var topLeft: Vector2F, var bottomRight: Vector2F)
