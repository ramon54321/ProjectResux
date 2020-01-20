package sux.common.serialization

import io.circe.generic.decoding.DerivedDecoder
import io.circe.generic.encoding.DerivedAsObjectEncoder
import shapeless.Lazy

import scala.collection.mutable

trait Kinded {
  val kind: String
}

class JsonSerializer[B <: Kinded] {
  import io.circe.parser.parse
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder, Json, JsonObject}

  private val codecs = mutable.HashMap[Any, (Any, Any, (Json) => Option[B])]()

  def addClass[T <: B](kind: String)(implicit encode: Lazy[DerivedAsObjectEncoder[T]], decode: Lazy[DerivedDecoder[T]]): JsonSerializer[B] = {
    codecs.put(kind, (deriveEncoder[T], deriveDecoder[T], (json: Json) => getActionFromJson[T](kind, json)))
    this
  }

  private def getEncoder[T](name: String): Encoder[T] = codecs(name).asInstanceOf[(Any, Any, Any)]._1.asInstanceOf[Encoder[T]]
  private def getDecoder[T](name: String): Decoder[T] = codecs(name).asInstanceOf[(Any, Any, Any)]._2.asInstanceOf[Decoder[T]]
  private def getJsonObject[T <: B](base: T): JsonObject = getEncoder[T](base.kind)(base).asObject.get
  private def addKind(jsonObject: JsonObject, base: B): JsonObject = jsonObject.add("kind", Json.fromString(base.kind))
  private def getFieldAsString(json: Json, field: String): Option[String] = json.hcursor.downField(field).as[String].toOption
  private def getActionFromJson[T](name: String, json: Json): Option[T] = {
    json.as[T](getDecoder(name)) match {
      case Right(obj) => Some(obj)
      case _ => None
    }
  }
  def toJson(base: B): String = {
    val jsonObject = getJsonObject(base)
    val jsonObjectWithKind  = addKind(jsonObject, base)
    val json = Json.fromJsonObject(jsonObjectWithKind)
    json.noSpaces
  }
  def fromJson(jsonString: String): Option[B] = {
    val json = parse(jsonString).getOrElse(Json.Null)
    val kind = getFieldAsString(json, "kind").getOrElse("Unknown")
    codecs(kind).asInstanceOf[(Any, Any, (Json) => Option[B])]._3(json)
  }
}