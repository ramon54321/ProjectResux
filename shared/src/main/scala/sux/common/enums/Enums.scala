package sux.common.enums

object Enums {
  object MoveSpeed {
    type MoveSpeed = Short
    val VerySlow: MoveSpeed = 0.toShort
    val Slow: MoveSpeed = 1.toShort
    val Normal: MoveSpeed = 2.toShort
    val Fast: MoveSpeed = 3.toShort
    val VeryFast: MoveSpeed = 4.toShort
  }
}
