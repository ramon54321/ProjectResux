package sux.common.math

import scala.collection.immutable.HashMap
import scala.math.{acos, atan2, cos, sin, sqrt}

case class Vec2D(x: Double, y: Double) {
  def asMutable(): MVec2D = {
    MVec2D(x, y)
  }
  def asVector2F(): Vec2F = {
    Vec2F(x.toFloat, y.toFloat)
  }

  def +(b: Vec2D): Vec2D = {
    Vec2D(x + b.x, y + b.y)
  }
}
case class MVec2D(var x: Double, var y: Double) {
  def asImmutable(): Vec2D = {
    Vec2D(x, y)
  }
  def asVector2F(): Vec2F = {
    Vec2F(x.toFloat, y.toFloat)
  }

  def +(b: MVec2D): Vec2D = {
    Vec2D(x + b.x, y + b.y)
  }
}
case class Vec2F(x: Float, y: Float) {
  def toSerializable: Vec2F.Serializable = HashMap("x" -> x, "y" -> y)
  def asMutable(): MVec2F = {
    MVec2F(x, y)
  }
  def asVec2D(): Vec2D = {
    Vec2D(x.toDouble, y.toDouble)
  }
  def asDeterministic(): DVec2F = {
    DVec2F(LUTF(0L -> x), LUTF(0L -> y))
  }
}
case class MVec2F(var x: Float, var y: Float) {
  def asImmutable(): Vec2F = {
    Vec2F(x, y)
  }
  def asVec2D(): Vec2D = {
    Vec2D(x.toDouble, y.toDouble)
  }
  def asDeterministic(): DVec2F = {
    DVec2F(LUTF(0L -> x), LUTF(0L -> y))
  }
}

case class DVec2F(private val x: LUTF, private val y: LUTF) extends Deterministic[Vec2F] {
  override def lookup(t: Long): Vec2F = Vec2F(x.lookup(t), y.lookup(t))
  def toSerializable: DVec2F.Serializable = HashMap("x" -> x.pointsSeq, "y" -> y.pointsSeq)
}

object DVec2F {
  type Serializable = HashMap[String, Seq[(Long, Float)]]
  def fromSerializable(serializable: Serializable): DVec2F =
    DVec2F(LUTF(serializable("x"):_*), LUTF(serializable("y"):_*))

  def apply(vectorPairs: (Long, Vec2F)*): DVec2F = {
    val xs: Seq[(Long, Float)] = vectorPairs.map(pair => (pair._1, pair._2.x))
    val ys: Seq[(Long, Float)] = vectorPairs.map(pair => (pair._1, pair._2.y))
    DVec2F(LUTF(xs:_*), LUTF(ys:_*))
  }

  def append(a: DVec2F, b: DVec2F): DVec2F = DVec2F(LUTF.append(a.x, b.x), LUTF.append(a.y, b.y))
  def extend(a: DVec2F, b: DVec2F): DVec2F = DVec2F(LUTF.extend(a.x, b.x), LUTF.extend(a.y, b.y))
}

object Vec2F {
  type Serializable = HashMap[String, Float]
  def fromSerializable(serializable: Serializable): Vec2F =
    Vec2F(serializable("x"), serializable("y"))

  def add(a: Vec2F, b: Vec2F): Vec2F = {
    Vec2F(a.x + b.x, a.y + b.y)
  }

  def subtract(a: Vec2F, b: Vec2F): Vec2F = {
    Vec2F(a.x - b.x, a.y - b.y)
  }

  def scale(a: Vec2F, factor: Float): Vec2F = {
    Vec2F(a.x * factor, a.y * factor)
  }

  def scaleX(a: Vec2F, factor: Float): Vec2F = {
    Vec2F(a.x * factor, a.y)
  }

  def scaleY(a: Vec2F, factor: Float): Vec2F = {
    Vec2F(a.x, a.y * factor)
  }

  def squareMagnitude(a: Vec2F): Float = {
    a.x * a.x + a.y * a.y
  }

  def magnitude(a: Vec2F): Float = {
    sqrt(squareMagnitude(a)).toFloat
  }

  def unit(a: Vec2F): Vec2F = {
    val mag = magnitude(a)
    Vec2F(a.x / mag, a.y / mag)
  }

  def direction(a: Vec2F, b: Vec2F): Vec2F = {
    val dir = subtract(b, a)
    unit(dir)
  }

  def dot(a: Vec2F, b: Vec2F): Float = {
    a.x * b.x + a.y * b.y
  }

  def angleBetween(a: Vec2F, b: Vec2F): Float = {
    val aMag = magnitude(a)
    val bMag = magnitude(b)
    acos(dot(a, b) / (aMag * bMag)).toFloat
  }

  def bisector(a: Vec2F, b: Vec2F): Vec2F = {
    val aUnit = unit(a)
    val bUnit = unit(b)
    unit(add(aUnit, bUnit))
  }

  /**
   * Returns the point inset between two vectors with a circle with radius r, where vectors a and b are tangential to the circle.
   */
  def insetPoint(a: Vec2F, b: Vec2F, r: Float): Vec2F = {
    val ang = angleBetween(a, b)
    val distance = r / sin(ang / 2)
    val bisec = bisector(a, b)
    scale(bisec, distance.toFloat)
  }

  /**
   * Returns vector a projected onto vector b from the perpendicular of vector b
   */
  def projection(a: Vec2F, b: Vec2F): Vec2F = {
    val bUnit = unit(b)
    val factor = dot(a, bUnit)
    scale(bUnit, factor)
  }

  /**
   * Returns the angle to a point from the origin or a given point
   */
  def angleTo(a: Vec2F, from: Vec2F = Vec2F(0f, 0f)): Float = {
    atan2(a.y - from.y, a.x - from.x).toFloat
  }

  /**
   * Returns the RHS perpendicular vector of vector a
   */
  def perpendicular(a: Vec2F): Vec2F = {
    unit(Vec2F(-a.y, a.x))
  }

  /**
   * Returns a vector given an angle from the x axis
   */
  def angleVector(a: Float): Vec2F = {
    Vec2F(cos(a).toFloat, sin(a).toFloat)
  }

  /**
   * Returns a vector from point a to point b
   */
  def fromPoints(a: Vec2F, b: Vec2F): Vec2F = {
    subtract(b, a)
  }

  def distance(a: Vec2F, b: Vec2F): Float = {
    magnitude(subtract(b, a))
  }

  def squareDistance(a: Vec2F, b: Vec2F): Float = {
    squareMagnitude(subtract(b, a))
  }
}

