package sux.scratch

case class Vec2F(x: Float, y: Float)
case class Vec2D(x: Double, y: Double)

trait Vector[T] {
  def add(a: T, b: T): T
  def scale(a: T, scale: Float): T
  def squareMagnitude(a: T): Float
}

object Vector {
  implicit val implVec2F: Vector[Vec2F] = new Vector[Vec2F]{
    override def add(a: Vec2F, b: Vec2F): Vec2F = Vec2F(a.x + b.x, a.y + b.y)
    override def scale(a: Vec2F, scale: Float): Vec2F = Vec2F(a.x * scale, a.y * scale)
    override def squareMagnitude(a: Vec2F): Float = a.x * a.x + a.y * a.y
  }
  implicit val implVec2D: Vector[Vec2D] = new Vector[Vec2D] {
    override def add(a: Vec2D, b: Vec2D): Vec2D = Vec2D(a.x + b.x, a.y + b.y)
    override def scale(a: Vec2D, scale: Float): Vec2D = Vec2D(a.x * scale, a.y * scale)
    override def squareMagnitude(a: Vec2D): Float = (a.x * a.x + a.y * a.y).toFloat
  }
  implicit class VectorUtils[T](a: T)(implicit impl: Vector[T]) {
    def add(b: T): T = impl.add(a, b)
    def subtract(b: T): T = impl.add(a, b.inverse())
    def scale(scale: Float): T = impl.scale(a, scale)
    def inverse(): T = impl.scale(a, -1f)
    def squareMagnitude(): Float = impl.squareMagnitude(a)
    def magnitude(): Float = math.sqrt(squareMagnitude().toDouble).toFloat
    def toVec2F(vec: Vec2D): Vec2F = Vec2F(vec.x.toFloat, vec.y.toFloat)
    def toVec2D(vec: Vec2F): Vec2D = Vec2D(vec.x.toDouble, vec.y.toDouble)
  }
}

object Pathfinder {
  import Vector._
  def getPath[T](start: T, end: T)(implicit vector: Vector[T]): T = start.add(end).inverse()
}

object VectorMain {
    import Vector._
    val path = Pathfinder.getPath(Vec2F(0f, 0f), Vec2F(4f, 5f))
    val path2 = Pathfinder.getPath(Vec2D(0.0, 0.0), Vec2D(4.0, 5.0))
}


















