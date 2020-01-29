package sux.client.utils

import org.scalajs.dom.{document, window}

object Browser {
  def resetWindow(): Unit = {
    document.body.innerHTML = ""
    document.body.style.margin = "0px"
    document.body.style.position = "fixed"
  }

  def getWindowWidth: Int = window.innerWidth.toInt

  def getWindowHeight: Int = window.innerHeight.toInt
}
