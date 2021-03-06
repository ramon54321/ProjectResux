package sux.client.rendering

import sux.common.math.{MVec2D, Vec2D, Vec2F}

import scala.collection.immutable.HashMap
import scala.util.Try

object Mapping {
  /**
   * Canvas Space is the pixel dimensions of the canvas, with the origin in the top left
   * Screen Space has the origin in the center and ranges from -1 to 1 from bottom to top corrected with the camera aspect ratio
   * World Space is the actual position of the entity from which logic is calculated
   */
  def worldSpaceToCanvasSpace(frameInfo: FrameInfo, worldPoint: Vec2F): Vec2D = screenSpaceToCanvasSpace(frameInfo.canvasSize, worldSpaceToScreenSpace(frameInfo.camera, worldPoint))
  def worldSpaceToCanvasSpace(frameInfo: FrameInfo, worldValue: Float): Double = screenSpaceToCanvasSpace(frameInfo.canvasSize, worldSpaceToScreenSpace(frameInfo.camera, worldValue))

  def canvasSpaceToWorldSpace(frameInfo: FrameInfo, canvasPoint: Vec2D): Vec2F = screenSpaceToWorldSpace(frameInfo.camera, canvasSpaceToScreenSpace(frameInfo.canvasSize, canvasPoint))
  def canvasSpaceToWorldSpace(frameInfo: FrameInfo, canvasValue: Double): Float = screenSpaceToWorldSpace(frameInfo.camera, canvasSpaceToScreenSpace(frameInfo.canvasSize, canvasValue))

  private def worldSpaceToScreenSpace(camera: Camera, worldPoint: Vec2F): Vec2F = {
    // worldSpace to translatedSpace
    val xTranslatedSpace = worldPoint.x - camera.panX
    val yTranslatedSpace = worldPoint.y - camera.panY

    // translatedSpace to scopeSpace
    val squareScope = camera.scope * camera.scope
    val xScopeSpace = xTranslatedSpace / squareScope
    val yScopeSpace = yTranslatedSpace / squareScope

    // scopeSpace to screenSpace
    val xScreenSpace = xScopeSpace / camera.aspectRatio
    val yScreenSpace = yScopeSpace
    Vec2F(xScreenSpace, yScreenSpace)
  }

  private def worldSpaceToScreenSpace(camera: Camera, worldValue: Float): Float = {
    worldValue
  }


  private def screenSpaceToWorldSpace(camera: Camera, screenPoint: Vec2F): Vec2F = {
    // screenSpace to scopeSpace
    val xScopeSpace = screenPoint.x * camera.aspectRatio
    val yScopeSpace = screenPoint.y

    // scopeSpace to translatedSpace
    val squareScope = camera.scope * camera.scope
    val xTranslatedSpace = xScopeSpace * squareScope
    val yTranslatedSpace = yScopeSpace * squareScope

    // translatedSpace to worldSpace
    val xWorldSpace = xTranslatedSpace + camera.panX
    val yWorldSpace = yTranslatedSpace + camera.panY

    Vec2F(xWorldSpace, yWorldSpace)
  }

  private def screenSpaceToWorldSpace(camera: Camera, screenValue: Float): Float = {
    screenValue
  }


  private def screenSpaceToCanvasSpace(canvasSize: MVec2D, screenPoint: Vec2F): Vec2D = {
    val x = (screenPoint.x * canvasSize.x / 2) + canvasSize.x / 2
    val y = (screenPoint.y * -canvasSize.y / 2) + canvasSize.y / 2
    Vec2D(x, y)
  }

  private def screenSpaceToCanvasSpace(canvasSize: MVec2D, screenValue: Float): Double = {
    screenValue.toDouble
  }


  private def canvasSpaceToScreenSpace(canvasSize: MVec2D, canvasPoint: Vec2D): Vec2F = {
    val x = (canvasPoint.x - canvasSize.x / 2) * 2 / canvasSize.x
    val y = (canvasPoint.y - canvasSize.y / 2) * 2 / -canvasSize.y
    Vec2F(x.toFloat, y.toFloat)
  }

  private def canvasSpaceToScreenSpace(canvasSize: MVec2D, canvasValue: Double): Float = {
    canvasValue.toFloat
  }


  def mapAngleToCanvas(angle: Float): Float = {
    -angle
  }

  def mapAngleToWorld(angle: Float): Float = {
    -angle
  }

  private val keyMap: Map[Int, String] = HashMap(
    16 -> "shift",
    17 -> "control",
    18 -> "option",
    65 -> "a",
    66 -> "b",
    67 -> "c",
    68 -> "d",
    69 -> "e",
    70 -> "f",
    71 -> "g",
    72 -> "h",
    73 -> "i",
    74 -> "j",
    75 -> "k",
    76 -> "l",
    77 -> "m",
    78 -> "n",
    79 -> "o",
    80 -> "p",
    81 -> "q",
    82 -> "r",
    83 -> "s",
    84 -> "t",
    85 -> "u",
    86 -> "v",
    87 -> "w",
    88 -> "x",
    89 -> "y",
    90 -> "z"
  )

  def mapKeyCodeToKey(keyCode: Int): String  = Try(keyMap(keyCode)).getOrElse("unknown")
}
