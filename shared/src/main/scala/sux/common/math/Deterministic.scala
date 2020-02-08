package sux.common.math

trait Deterministic[T] {
  def lookup(x: Long): T
}
