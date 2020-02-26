package sux.common.utils

sealed trait OptionOp[A, B]
case class SomeOp[A, B](option: Option[A], op: A => B) extends OptionOp[A, B]
case class NoneOp[A, B](option: Option[A], op: () => B) extends OptionOp[A, B]

object OptionExtensions {
  def doOp[A, B](optionOp: OptionOp[A, B]): Boolean = {
    optionOp match {
      case SomeOp(option, op) if option.isDefined =>
        op(option.get)
        true
      case NoneOp(option, op) if option.isEmpty =>
        op()
        true
      case _ => false
    }
  }
  def doAll(optionOps: OptionOp[_, _]*): Unit = {
    val lastIndex = optionOps.size - 1
    var i = 0
    while (i <= lastIndex) {
      doOp(optionOps(i))
      i += 1
    }
  }
  def doFirst(optionOps: OptionOp[_, _]*): Unit = {
    val lastIndex = optionOps.size - 1
    var i = 0
    while (i <= lastIndex) {
      if (doOp(optionOps(i))) return
      i += 1
    }
  }
  implicit class OptionExtension[A](option: Option[A]) {
    def someOp[B](op: A => B): SomeOp[A, B] = SomeOp[A, B](option, op)
    def noneOp[B](op: () => B): NoneOp[A, B] = NoneOp[A, B](option, op)
    def handle[B](someOp: A => _, noneOp: () => _): Unit = option match {
      case Some(value) => someOp(value)
      case None => noneOp()
    }
  }
}
