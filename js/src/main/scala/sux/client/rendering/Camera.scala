package sux.client.rendering

class Camera {
  var panX: Float = 0f
  var panY: Float = 0f
  val panRate: Float = 0.025f
  var scope: Float = 15f
  val scopeSquareMax: Float = 500f
  val scopeSquareMin: Float = 4f
  val scopeRate: Float = 0.2f
  var aspectRatio: Float = 0f
}
