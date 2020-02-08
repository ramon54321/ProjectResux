package sux.common.math

import scala.collection.Searching._
import scala.collection.immutable.TreeMap
import scala.math.Ordering.Long
import scala.reflect.ClassTag

import sux.common.utils.CollectionExtensions._

protected class LUT[T](points: TreeMap[Long, T], interpolation: (Long, Long, T, T, Long) => T)(implicit classTag: ClassTag[T]) {
  private val keys = points.keys.toArray
  private val values = points.values.toArray
  def lookup(t: Long): T = keys.search(t) match {
    case Found(foundIndex) => values(foundIndex)
    case InsertionPoint(insertionPoint) =>
      val lowerIndex = math.max(0, insertionPoint - 1)
      val upperIndex = math.min(values.lastIndex(), insertionPoint)
      val lowerKey = keys(lowerIndex)
      val upperKey = keys(upperIndex)
      val lowerValue = values(lowerIndex)
      val upperValue = values(upperIndex)
      interpolation(lowerKey, upperKey, lowerValue, upperValue, t)
  }
  def pointsSeq: Seq[(Long, T)] = keys.zip(values)
}

class LUTF(points: TreeMap[Long, Float], interpolation: (Long, Long, Float, Float, Long) => Float) extends LUT[Float](points, interpolation)

object LUTF {
  val identity = LUTF(0L -> 0f, 1L -> 1f)
  val inverse = LUTF(0L -> 1f, 1L -> 0f)
  def apply(points: (Long, Float)*): LUTF = new LUTF(TreeMap(points:_*), Interpolation.interpolate)
}
