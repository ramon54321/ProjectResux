package sux.client.rendering

import sux.client.utils.Browser
import sux.common.math.{MutableRectV2F, MutableVector2D, Vector2D, Vector2F}
import sux.common.state.WorldState
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.{CanvasRenderingContext2D, HTMLCanvasElement, UIEvent}
import sux.client.InterfaceState
import sux.client.rendering.layers.{BackgroundGradientRenderLayer, BackgroundGridRenderLayer, DebugSpacialRenderLayer, RenderLayer}

import scala.collection.mutable

class CanvasRenderer(private val worldState: WorldState, private var camera: Camera) {
  private val canvas: HTMLCanvasElement = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
  private val drawInfo: DrawInfo = DrawInfo(
    canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D],
    MutableVector2D(0.0, 0.0),
    MutableRectV2F(Vector2F(0f, 0f), Vector2F(0f, 0f)),
    camera
  )

  private val renderLayers: mutable.MutableList[RenderLayer] = new mutable.MutableList[RenderLayer]

  // Initial Setup
  Browser.resetWindow()
  setCanvasDimensions()
  document.body.appendChild(canvas)
  initRenderLayers()

  // Listeners
  window.onresize = (event: UIEvent) => {
    setCanvasDimensions()
    renderLayers.foreach(_.onCanvasResize(drawInfo))
  }
  document.onkeydown = (key) => InterfaceState.setKeyDown(Mapping.mapKeyCodeToKey(key.keyCode))
  document.onkeyup = (key) => InterfaceState.setKeyUp(Mapping.mapKeyCodeToKey(key.keyCode))

  // Start Draw
  draw(drawInfo)

  private def initRenderLayers(): Unit = {
    renderLayers += new BackgroundGradientRenderLayer(drawInfo)
    renderLayers += new BackgroundGridRenderLayer()
    renderLayers += new DebugSpacialRenderLayer()
  }

  private def setCanvasDimensions(): Unit = {
    val screenWidth = Browser.getWindowWidth()
    val screenHeight = Browser.getWindowHeight()
    camera.aspectRatio = screenWidth / screenHeight.toFloat
    canvas.width = screenWidth * 2
    canvas.height = screenHeight * 2
    canvas.style.width = s"${screenWidth}px"
    canvas.style.height = s"${screenHeight}px"
    drawInfo.canvasSize.x = screenWidth
    drawInfo.canvasSize.y = screenHeight
    drawInfo.context.scale(2, 2)
  }

  // TODO: Move out of renderer
  private def update(): Unit = {
    val squareScope = drawInfo.camera.scope * drawInfo.camera.scope
    if (InterfaceState.getKey("q") && squareScope < camera.scopeSquareMax) {
      drawInfo.camera.scope += camera.scopeRate
    }
    if (InterfaceState.getKey("e") && squareScope > camera.scopeSquareMin) {
      drawInfo.camera.scope -= camera.scopeRate
    }
    if (InterfaceState.getKey("d")) {
      drawInfo.camera.panX += camera.panRate * squareScope
    }
    if (InterfaceState.getKey("a")) {
      drawInfo.camera.panX -= camera.panRate * squareScope
    }
    if (InterfaceState.getKey("w")) {
      drawInfo.camera.panY += camera.panRate * squareScope
    }
    if (InterfaceState.getKey("s")) {
      drawInfo.camera.panY -= camera.panRate * squareScope
    }
  }

  private def clear(): Unit = {
    drawInfo.context.clearRect(0.0, 0.0, drawInfo.canvasSize.x, drawInfo.canvasSize.y)
  }

  private def draw(drawInfo: DrawInfo): Unit = {
    window.requestAnimationFrame((_: Double) => draw(drawInfo))

    // Update
    update()
    drawInfo.screenRect.topLeft = Mapping.canvasSpaceToWorldSpace(drawInfo, Vector2D(0.0, 0.0))
    drawInfo.screenRect.bottomRight = Mapping.canvasSpaceToWorldSpace(drawInfo, Vector2D(drawInfo.canvasSize.x, drawInfo.canvasSize.y))

    // Render
    clear()

    renderLayers.foreach(_.draw(drawInfo))

    drawInfo.context.fillStyle = "rgba(255, 255, 255, 0.85)"
  }
}
