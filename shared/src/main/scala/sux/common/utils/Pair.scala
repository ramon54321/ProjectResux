package sux.common.utils

import cats.implicits._
import CollectionExtensions._

object Pair {

  trait PairError
  case class EmptyPairListError() extends PairError
  case class NoSurroundingPairError(lookup: Long) extends PairError

  def getLowerBoundPairIndex[T](pairs: List[(Long, T)], lookup: Long): Either[EmptyPairListError, Int] = {
    if (pairs.isEmpty) {
      return Left(EmptyPairListError())
    }
    val index = pairs.indexWhere(_._1 > lookup)
    val lastIndex = pairs.lastIndex()
    val lookupAbove = lookup > pairs.head._1
    index match {
      case _ if index < 0 => if (lookupAbove) Right(lastIndex) else Right(0)
      case _ if index <= lastIndex => Right(index)
      case _ if index > lastIndex => Right(lastIndex)
    }
  }

  def getUpperBoundPairIndex[T](pairs: List[(Long, T)], lookup: Long): Either[EmptyPairListError, Int] = {
    if (pairs.isEmpty) {
      return Left(EmptyPairListError())
    }
    val index = pairs.indexWhere(_._1 > lookup)
    val lastIndex = pairs.lastIndex()
    val lookupAbove = lookup > pairs.head._1
    index match {
      case _ if index < 0 => if (lookupAbove) Right(lastIndex) else Right(0)
      case _ if index <= lastIndex => Right(index)
      case _ if index > lastIndex => Right(lastIndex)
    }
  }

  def getSurroundingPairs[T](pairs: List[(Long, T)], lookup: Long): Either[PairError, ((Long, T), (Long, T))] = {
    if (pairs.isEmpty) {
      Left(EmptyPairListError())
    }
    val firstIndex = getLowerBoundPairIndex(pairs, lookup)
    val secondIndex = getUpperBoundPairIndex(pairs, lookup)
    (firstIndex, secondIndex) match {
      case (Right(a), Right(b)) => Right((pairs.get(a).get, pairs.get(b).get))
      case _ => Left(NoSurroundingPairError(lookup))
    }
  }
}

