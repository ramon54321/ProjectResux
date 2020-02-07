package sux.client.debug

object RenderStatistics {
  var drawSpriteCount = 0
  var drawLineCount = 0
  var drawCircleCount = 0
  var drawTextCount = 0

  def drawCallCount: Int = drawSpriteCount + drawLineCount + drawCircleCount + drawTextCount

  def reset(): Unit = {
    drawSpriteCount = 0
    drawLineCount = 0
    drawCircleCount = 0
    drawTextCount = 0
  }

  def print(): Unit = {
    println(f"drawSpriteCount: ${drawSpriteCount}")
    println(f"drawLineCount: ${drawLineCount}")
    println(f"drawCircleCount: ${drawCircleCount}")
    println(f"drawTextCount: ${drawTextCount}")
    println(f"drawCallCount: ${drawCallCount}")
  }
}
