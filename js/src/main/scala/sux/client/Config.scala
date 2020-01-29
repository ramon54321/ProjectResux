package sux.client

import scala.scalajs.js

object Config {
  private val config = js.Dynamic.global.document.selectDynamic("config")

  val label: String = init("label").getOrElse("Unknown").asInstanceOf[String]
  val version: String = init("version").getOrElse("Unknown").asInstanceOf[String]
  val commitHash: String = init("commitHash").getOrElse("Unknown").asInstanceOf[String]
  val commitAuthor: String = init("commitAuthor").getOrElse("Unknown").asInstanceOf[String]
  val clientBuildHash: String = init("clientBuildHash").getOrElse("Unknown").asInstanceOf[String]
  val clientBuildHashShort: String = init("clientBuildHash").map(_.asInstanceOf[String].take(8)).getOrElse("Unknown")
  val buildDate: String = init("buildDate").getOrElse("Unknown").asInstanceOf[String]
  val serverHost: String = init("serverHost").getOrElse("localhost").asInstanceOf[String]
  val serverPort: Int = init("serverPort").getOrElse(8080).asInstanceOf[Int]

  private def init(key: String): Option[js.Dynamic] = {
    val value = config.selectDynamic(key)
    if (js.isUndefined(value)) {
      println(f"[Config] Can not find value for key: $key")
      None
    } else Some(value)
  }
}
