package sux.client.debug

import org.scalajs.dom.window

import scala.collection.mutable

class Timer(val name: String, val reportInterval: Int = 120) {
  private val measure = s"${name}_measure"
  private val startMark = s"${name}_start"
  private val endMark = s"${name}_end"

  private val measurements = new mutable.ListBuffer[Double]()

  def markStart(): Unit = {
    window.performance.mark(startMark)
  }

  def markEnd(): Unit = {
    window.performance.mark(endMark)
    window.performance.measure(measure, startMark, endMark)
    val measurement = window.performance.getEntriesByName(measure, "measure").selectDynamic("0").selectDynamic("duration").asInstanceOf[Double]
    window.performance.clearMeasures(measure)
    window.performance.clearMarks(startMark)
    window.performance.clearMarks(endMark)
    measurements.append(measurement)
    if (measurements.length >= reportInterval) report()
  }

  private def report(): Unit = {
    val averageMeasurement = measurements.sum / measurements.length
    measurements.clear()
    println(s"${name}: ${averageMeasurement.formatted("%1.3f")}")
  }
}
