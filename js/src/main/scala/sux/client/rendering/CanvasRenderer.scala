package sux.client.rendering

import sux.client.utils.Browser
import sux.common.math.{MVec2D, MutableRectV2F, Vec2D, Vec2F}
import sux.common.state.WorldState
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.{HTMLCanvasElement, UIEvent}
import sux.client.InterfaceState.{clearHoverNode, clearContextMenu, getMouseCanvasPosition}
import sux.client.debug.layers.{DebugBuildInfoRenderLayer, DebugSpacialRenderLayer}
import sux.client.debug.{RenderStatistics, Timer}
import sux.client.gameplay.ContextMenu
import sux.client.{Config, InterfaceOrchestration, InterfaceState}
import sux.client.rendering.extensions.ExtendedCanvasRenderingContext2D
import sux.client.rendering.layers.{BackgroundGradientRenderLayer, BackgroundGridRenderLayer, ContextMenuRenderLayer, EntitiesRenderLayer, RenderLayer}
import sux.common.utils.OptionExtensions._

import scala.collection.mutable

class CanvasRenderer(private val worldState: WorldState, private var camera: Camera) {
  private val canvas: HTMLCanvasElement = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
  private val drawInfo: DrawInfo = DrawInfo(
    canvas.getContext("2d").asInstanceOf[ExtendedCanvasRenderingContext2D],
    MVec2D(0.0, 0.0),
    MutableRectV2F(Vec2F(0f, 0f), Vec2F(0f, 0f)),
    worldState,
    0L,
    camera
  )

  private val renderLayers: mutable.MutableList[RenderLayer] = new mutable.MutableList[RenderLayer]

  private val updateTimer = new Timer("Update")
  private val renderTimer = new Timer("Render")

  private var frameCounter = 0

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
  document.onkeydown = key => InterfaceState.setKeyDown(Mapping.mapKeyCodeToKey(key.keyCode))
  document.onkeyup = key => InterfaceState.setKeyUp(Mapping.mapKeyCodeToKey(key.keyCode))
  document.onmousemove = mouse => InterfaceState.setMouseCanvasPosition(mouse.clientX, mouse.clientY)
  document.onmousedown = _ => InterfaceState.setClickLeft()

  // Start Draw
  draw(drawInfo)

  private def initRenderLayers(): Unit = {
    renderLayers += new BackgroundGradientRenderLayer(drawInfo)
    renderLayers += new BackgroundGridRenderLayer()
    renderLayers += new DebugSpacialRenderLayer()
    renderLayers += new DebugBuildInfoRenderLayer()
//    renderLayers += new DebugSpritesRenderLayer()
    renderLayers += new EntitiesRenderLayer()
    renderLayers += new ContextMenuRenderLayer()
  }

  private def setCanvasDimensions(): Unit = {
    val screenWidth = Browser.getWindowWidth
    val screenHeight = Browser.getWindowHeight
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
  private def slowUpdate(): Unit = {
    RenderStatistics.print()
  }

  private def update(): Unit = {
    RenderStatistics.reset()

    // Global Perframe Calculations
    drawInfo.worldTime = System.currentTimeMillis()
    drawInfo.worldState.getEntityByNearest(Mapping.canvasSpaceToWorldSpace(
      drawInfo, InterfaceState.getMouseCanvasPosition.asImmutable()), drawInfo.worldTime) match {
      case Some(entity) =>
        InterfaceState.setNearestHoverEntity(entity)
        val distance = Vec2F.distance(InterfaceState.getMouseCanvasPosition.asVector2F(), Mapping.worldSpaceToCanvasSpace(drawInfo, entity.position.lookup(drawInfo.worldTime)).asVector2F())
        if (distance < Config.hoverRadius) InterfaceState.setHoverEntity(entity)
        else InterfaceState.clearHoverEntity()
      case None =>
        InterfaceState.clearNearestHoverEntity()
        InterfaceState.clearHoverEntity()
    }

    // Keys
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

    if (InterfaceState.getKeyDown("f")) InterfaceOrchestration.toggleContextMenu()

    // Mouse
    val hoverNode = InterfaceState.getHoverNode
    val hoverEntity = InterfaceState.getHoverEntity
    if (InterfaceState.getClickLeft) {
      if (hoverNode.isDefined) InterfaceOrchestration.clickNode(hoverNode.get)
      else if (hoverEntity.isDefined) InterfaceOrchestration.clickEntity(hoverEntity.get)
      else InterfaceOrchestration.deselectAll()
    }

    // TODO: Fix grid line width with save and restore

    // Late Update (After Input)
    val lateSquareScope = drawInfo.camera.scope * drawInfo.camera.scope
    drawInfo.camera.scale = (500 / Config.spritePixelsPerMeter) / lateSquareScope
    drawInfo.screenWorldRect.topLeft = Mapping.canvasSpaceToWorldSpace(drawInfo, Vec2D(0.0, 0.0))
    drawInfo.screenWorldRect.bottomRight = Mapping.canvasSpaceToWorldSpace(drawInfo, Vec2D(drawInfo.canvasSize.x, drawInfo.canvasSize.y))

    // Reset Keys
    InterfaceState.resetKeys()
    InterfaceState.resetMouse()
  }

  private def clear(): Unit = {
    drawInfo.context.clearRect(0.0, 0.0, drawInfo.canvasSize.x, drawInfo.canvasSize.y)
  }

  private def draw(drawInfo: DrawInfo): Unit = {
    window.requestAnimationFrame((_: Double) => draw(drawInfo))
    frameCounter += 1
    if (frameCounter % 120 == 0) slowUpdate()

    // Update
    updateTimer.markStart()
    update()
    updateTimer.markEnd()

    // Render
    renderTimer.markStart()
    clear()

    renderLayers.foreach(_.draw(drawInfo))

    drawInfo.context.fillStyle = "rgba(255, 255, 255, 0.85)"
    renderTimer.markEnd()
  }
}
