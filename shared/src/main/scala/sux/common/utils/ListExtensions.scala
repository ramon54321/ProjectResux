package sux.common.utils

object ListExtensions {
  implicit class ExtendedList(list: List[_]) {
    def lastIndex(): Int = list.size - 1
  }
}
