package sux.client.rendering

import sux.client.utils.Browser
import sux.common.math.{MVec2D, MutableRectV2F, Vec2D, Vec2F}
import sux.common.state.WorldState
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.{HTMLCanvasElement, UIEvent}
import sux.client.debug.layers.{DebugBuildInfoRenderLayer, DebugSpacialRenderLayer}
import sux.client.debug.{RenderStatistics, Timer}
import sux.client.{Config, InterfaceOrchestration, InterfaceState}
import sux.client.rendering.extensions.ExtendedCanvasRenderingContext2D
import sux.client.rendering.layers.{BackgroundGradientRenderLayer, BackgroundGridRenderLayer, ContextMenuRenderLayer, EntitiesRenderLayer, RenderLayer}

import scala.collection.mutable

class CanvasRenderer(private val worldState: WorldState, private var camera: Camera) {
  private val canvas: HTMLCanvasElement = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
  private val frameInfo: FrameInfo = FrameInfo(
    canvas.getContext("2d").asInstanceOf[ExtendedCanvasRenderingContext2D],
    MVec2D(0.0, 0.0),
    MutableRectV2F(Vec2F(0f, 0f), Vec2F(0f, 0f)),
    worldState,
    0L,
    camera,
    Vec2D(0,0),
    Vec2F(0,0),
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
    renderLayers.foreach(_.onCanvasResize(frameInfo))
  }
  document.onkeydown = key => InterfaceState.setKeyDown(Mapping.mapKeyCodeToKey(key.keyCode))
  document.onkeyup = key => InterfaceState.setKeyUp(Mapping.mapKeyCodeToKey(key.keyCode))
  document.onmousemove = mouse => InterfaceState.setMouseCanvasPosition(mouse.clientX, mouse.clientY)
  document.onmousedown = _ => InterfaceState.setClickLeft()

  // Start Draw
  draw(frameInfo)

  private def initRenderLayers(): Unit = {
    renderLayers += new BackgroundGradientRenderLayer(frameInfo)
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
    frameInfo.canvasSize.x = screenWidth
    frameInfo.canvasSize.y = screenHeight
    frameInfo.context.scale(2, 2)
  }

  // TODO: Move out of renderer
  private def slowUpdate(): Unit = {
    RenderStatistics.print()
  }

  private def update(): Unit = {
    RenderStatistics.reset()

    // Global Per-frame Calculations
    // World Time
    frameInfo.worldTime = System.currentTimeMillis()

    // Mouse Position
    frameInfo.mouseCanvasPosition = InterfaceState.getMouseCanvasPosition.asImmutable()
    frameInfo.mouseWorldPosition = Mapping.canvasSpaceToWorldSpace(frameInfo, frameInfo.mouseCanvasPosition)

    // Hover and Selections
    frameInfo.worldState.getEntityByNearest(frameInfo.mouseWorldPosition, frameInfo.worldTime) match {
      case Some(entity) =>
        InterfaceState.setNearestHoverEntity(entity)
        // TODO: Add utility for getting entity canvas position
        val distance = Vec2F.distance(frameInfo.mouseCanvasPosition.asVector2F(),
          Mapping.worldSpaceToCanvasSpace(frameInfo, entity.position.lookup(frameInfo.worldTime)).asVector2F())
        if (distance < Config.hoverRadius) InterfaceState.setHoverEntity(entity)
        else InterfaceState.clearHoverEntity()
      case None =>
        InterfaceState.clearNearestHoverEntity()
        InterfaceState.clearHoverEntity()
    }

    // Keys
    val squareScope = frameInfo.camera.scope * frameInfo.camera.scope
    if (InterfaceState.getKey("q") && squareScope < camera.scopeSquareMax) {
      frameInfo.camera.scope += camera.scopeRate
    }
    if (InterfaceState.getKey("e") && squareScope > camera.scopeSquareMin) {
      frameInfo.camera.scope -= camera.scopeRate
    }
    if (InterfaceState.getKey("d")) {
      frameInfo.camera.panX += camera.panRate * squareScope
    }
    if (InterfaceState.getKey("a")) {
      frameInfo.camera.panX -= camera.panRate * squareScope
    }
    if (InterfaceState.getKey("w")) {
      frameInfo.camera.panY += camera.panRate * squareScope
    }
    if (InterfaceState.getKey("s")) {
      frameInfo.camera.panY -= camera.panRate * squareScope
    }

    if (InterfaceState.getKeyDown("f")) InterfaceOrchestration.toggleContextMenu(frameInfo)

    // Mouse
    val hoverNode = InterfaceState.getHoverNode
    val hoverEntity = InterfaceState.getHoverEntity
    if (InterfaceState.getClickLeft) {
      if (hoverNode.isDefined) InterfaceOrchestration.clickNode(frameInfo, hoverNode.get)
      else if (hoverEntity.isDefined) InterfaceOrchestration.clickEntity(hoverEntity.get)
      else InterfaceOrchestration.deselectAll()
    }

    // Late Update (After Input)
    val lateSquareScope = frameInfo.camera.scope * frameInfo.camera.scope
    frameInfo.camera.scale = (500 / Config.spritePixelsPerMeter) / lateSquareScope
    frameInfo.screenWorldRect.topLeft = Mapping.canvasSpaceToWorldSpace(frameInfo, Vec2D(0.0, 0.0))
    frameInfo.screenWorldRect.bottomRight = Mapping.canvasSpaceToWorldSpace(frameInfo, Vec2D(frameInfo.canvasSize.x, frameInfo.canvasSize.y))

    // Reset Keys
    InterfaceState.resetKeys()
    InterfaceState.resetMouse()
  }

  private def clear(): Unit = {
    frameInfo.context.clearRect(0.0, 0.0, frameInfo.canvasSize.x, frameInfo.canvasSize.y)
  }

  private def draw(frameInfo: FrameInfo): Unit = {
    window.requestAnimationFrame((_: Double) => draw(frameInfo))
    frameCounter += 1
    if (frameCounter % 120 == 0) slowUpdate()

    // Update
    updateTimer.markStart()
    update()
    updateTimer.markEnd()

    // Render
    renderTimer.markStart()
    clear()

    renderLayers.foreach(renderLayer => {
      frameInfo.context.save()
      renderLayer.draw(frameInfo)
      frameInfo.context.restore()
    })

    frameInfo.context.fillStyle = "rgba(255, 255, 255, 0.85)"
    renderTimer.markEnd()
  }
}
