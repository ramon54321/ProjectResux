package sux.common.utils

object CollectionExtensions {
  implicit class ExtendedList(list: List[_]) {
    def lastIndex(): Int = list.size - 1
  }
  implicit class ExtendedArray(array: Array[_]) {
    def lastIndex(): Int = array.size - 1
  }
}
