package sux.scratch

import scala.util.Try
import cats.Applicative
import cats.implicits.catsStdInstancesForOption

object Store {
  println()

  def getDiscountedQuote(initialQuote: Double): Double =
    if (initialQuote > 50.0) initialQuote * 0.80
    else initialQuote

  def formatUSD(value: Double): Option[String] = Try(f"USD $value%.2f").toOption

  def insuranceRateQuote(age: Int, ticketCount: Int): Double = age * ticketCount

  def parseInsuranceRateQuote(age: String, ticketCount: String): Option[Double] = {
    val optAge: Option[Int] = Try(age.toInt).toOption
    val optTicketCount: Option[Int] = Try(ticketCount.toInt).toOption
    Applicative[Option].map2(optAge, optTicketCount)(insuranceRateQuote)
  }

  val quote = parseInsuranceRateQuote("34", "2")
    .map(getDiscountedQuote)
    .flatMap(formatUSD)
    .getOrElse("Could not get quote")

  println(quote)
}















