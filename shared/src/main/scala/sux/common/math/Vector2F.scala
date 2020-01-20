package sux.common.math

import scala.math.{sqrt, cos, sin, acos, atan2}

case class Vector2D(x: Double, y: Double) {
  def asMutable(): MutableVector2D = {
    MutableVector2D(x, y)
  }
  def asVector2F(): Vector2F = {
    Vector2F(x.toFloat, y.toFloat)
  }
}
case class MutableVector2D(var x: Double, var y: Double) {
  def asImmutable(): Vector2D = {
    Vector2D(x, y)
  }
  def asVector2F(): Vector2F = {
    Vector2F(x.toFloat, y.toFloat)
  }
}
case class Vector2F(x: Float, y: Float) {
  def asMutable(): MutableVector2F = {
    MutableVector2F(x, y)
  }
  def asVector2D(): Vector2D = {
    Vector2D(x.toDouble, y.toDouble)
  }
}
case class MutableVector2F(var x: Float, var y: Float) {
  def asImmutable(): Vector2F = {
    Vector2F(x, y)
  }
  def asVector2D(): Vector2D = {
    Vector2D(x.toDouble, y.toDouble)
  }
}

object Vector2F {
  def add(a: Vector2F, b: Vector2F): Vector2F = {
    Vector2F(a.x + b.x, a.y + b.y)
  }

  def subtract(a: Vector2F, b: Vector2F): Vector2F = {
    Vector2F(a.x - b.x, a.y - b.y)
  }

  def scale(a: Vector2F, factor: Float): Vector2F = {
    Vector2F(a.x * factor, a.y * factor)
  }

  def scaleX(a: Vector2F, factor: Float): Vector2F = {
    Vector2F(a.x * factor, a.y)
  }

  def scaleY(a: Vector2F, factor: Float): Vector2F = {
    Vector2F(a.x, a.y * factor)
  }

  def squareMagnitude(a: Vector2F): Float = {
    a.x * a.x + a.y * a.y
  }

  def magnitude(a: Vector2F): Float = {
    sqrt(squareMagnitude(a)).toFloat
  }

  def unit(a: Vector2F): Vector2F = {
    val mag = magnitude(a)
    Vector2F(a.x / mag, a.y / mag)
  }

  def direction(a: Vector2F, b: Vector2F): Vector2F = {
    val dir = subtract(b, a)
    unit(dir)
  }

  def dot(a: Vector2F, b: Vector2F): Float = {
    a.x * b.x + a.y * b.y
  }

  def angleBetween(a: Vector2F, b: Vector2F): Float = {
    val aMag = magnitude(a)
    val bMag = magnitude(b)
    acos(dot(a, b) / (aMag * bMag)).toFloat
  }

  def bisector(a: Vector2F, b: Vector2F): Vector2F = {
    val aUnit = unit(a)
    val bUnit = unit(b)
    unit(add(aUnit, bUnit))
  }

  /**
   * Returns the point inset between two vectors with a circle with radius r, where vectors a and b are tangential to the circle.
   */
  def insetPoint(a: Vector2F, b: Vector2F, r: Float): Vector2F = {
    val ang = angleBetween(a, b)
    val distance = r / sin(ang / 2)
    val bisec = bisector(a, b)
    scale(bisec, distance.toFloat)
  }

  /**
   * Returns vector a projected onto vector b from the perpendicular of vector b
   */
  def projection(a: Vector2F, b: Vector2F): Vector2F = {
    val bUnit = unit(b)
    val factor = dot(a, bUnit)
    scale(bUnit, factor)
  }

  /**
   * Returns the angle to a point from the origin or a given point
   */
  def angleTo(a: Vector2F, from: Vector2F = Vector2F(0f, 0f)): Float = {
    atan2(a.y - from.y, a.x - from.x).toFloat
  }

  /**
   * Returns the RHS perpendicular vector of vector a
   */
  def perpendicular(a: Vector2F): Vector2F = {
    unit(Vector2F(-a.y, a.x))
  }

  /**
   * Returns a vector given an angle from the x axis
   */
  def angleVector(a: Float): Vector2F = {
    Vector2F(cos(a).toFloat, sin(a).toFloat)
  }

  /**
   * Returns a vector from point a to point b
   */
  def fromPoints(a: Vector2F, b: Vector2F): Vector2F = {
    subtract(b, a)
  }

  def distance(a: Vector2F, b: Vector2F): Float = {
    magnitude(subtract(b, a))
  }

  def squareDistance(a: Vector2F, b: Vector2F): Float = {
    squareMagnitude(subtract(b, a))
  }
}

