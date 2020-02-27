package sux.scratch

import sux.scratch.MoveSpeed.MoveSpeed

//sealed trait OptionOp[A, B]
//case class SomeOp[A, B](option: Option[A], op: A => B) extends OptionOp[A, B]
//case class NoneOp[A, B](option: Option[A], op: () => B) extends OptionOp[A, B]

object OptionExtensions {
//  def doOp[A, B](optionOp: OptionOp[A, B]): Boolean = {
//    optionOp match {
//      case SomeOp(option, op) if option.isDefined =>
//        op(option.get)
//        true
//      case NoneOp(option, op) if option.isEmpty =>
//        op()
//        true
//      case _ => false
//    }
//  }
//  def doAll(optionOps: OptionOp[_, _]*): Unit = {
//    val lastIndex = optionOps.size - 1
//    var i = 0
//    while (i <= lastIndex) {
//      doOp(optionOps(i))
//      i += 1
//    }
//  }
//  def doFirst(optionOps: OptionOp[_, _]*): Unit = {
//    val lastIndex = optionOps.size - 1
//    var i = 0
//    while (i <= lastIndex) {
//      if (doOp(optionOps(i))) return
//      i += 1
//    }
//  }
  implicit class OptionExtension[A](option: Option[A]) {
//    def someOp[B](op: A => B): SomeOp[A, B] = SomeOp[A, B](option, op)
//    def noneOp[B](op: () => B): NoneOp[A, B] = NoneOp[A, B](option, op)
//    def handle[B](someOp: A => _, noneOp: () => _): Unit = option match {
//      case Some(value) => someOp(value)
//      case None => noneOp()
//    }

    private var hasDoneOp = false

    def doOp(op: A => _): OptionExtension[A] = {
      option match {
        case Some(value) =>
          op(value)

        case None =>
      }
      hasDoneOp = true
      this
    }

//    def elseDoOp(op: A => _): OptionExtension[A] = {
//
//    }

    def toOption: Option[A] = option
  }
}

object MoveSpeed {
  type MoveSpeed = String
  val slow: MoveSpeed = "Slow"
  val fast: MoveSpeed = "Fast"
}

object Main extends App {


  val speed: MoveSpeed = MoveSpeed.fast

  println("Hello world 2!")



//  import OptionExtensions._
//
//  val leftClick = Some()
//  val hoverNode = Some("Andy")
//  val hoverEntity = None

//  if (leftClick.isDefined) {
//    if (hoverNode.isDefined) selectNode(hoverNode.get)
//    else if (hoverEntity.isDefined) selectEntity(hoverEntity.get)
//    else deselectAll()
//  }

//  if (leftClick.isDefined) {
//    hoverNode
//      .doOp(println)
//      .elseDoOp(println("No Hover Node"))
//      .elseDoOp(println())
//  }

//  println(hoverNode.name)
//  hoverNode.doOp((node: String) => println("World"))
//  println(hoverNode.name)


//  val a = None
//  val b = Some(65)
//  val c = Some("Hello")
//  val d: Option[String] = None
//
//  println("\nDoFirst")
//  doFirst(
//    a.someOp(println),
//    b.someOp(x => println(s"Hey ${x * 5}")),
//    d.noneOp(() => println("D None")),
//    d.someOp(d => println(s"D is $d"))
//  )
//
//  println("\nDoAll")
//  doAll(
//    a.someOp(println),
//    b.someOp(x => println(s"Hey ${x * 5}")),
//    d.noneOp(() => println("D None")),
//    d.someOp(d => println(s"D is $d"))
//  )
//
//  println("\nHandle")
//  c.handle((c: String) => println(s"Handling C which is $c"), () => println("No C"))
}
















