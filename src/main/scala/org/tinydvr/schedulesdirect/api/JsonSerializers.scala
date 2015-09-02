package org.tinydvr.schedulesdirect.api

import net.liftweb.json.JsonAST.JString
import net.liftweb.json._
import org.joda.time.{DateTime, LocalDate}

// inspired by http://blog.knoldus.com/2012/05/18/liftweb-json-serialization-and-deserialization-using-scala/
class DateTimeSerializer extends Serializer[DateTime] {
  private val Class = classOf[DateTime]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DateTime] = {
    case (TypeInfo(Class, _), json) => json match {
      case JString(str) => SchedulesDirectAPIClient.dateTimeFormatter.parseDateTime(str)
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: DateTime => JString(SchedulesDirectAPIClient.dateTimeFormatter.print(x))
  }
}

class LocalDateSerializer extends Serializer[LocalDate] {
  private val Class = classOf[LocalDate]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), LocalDate] = {
    case (TypeInfo(Class, _), json) => json match {
      case JString(str) => SchedulesDirectAPIClient.localDateFormatter.parseLocalDate(str)
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: LocalDate => JString(SchedulesDirectAPIClient.localDateFormatter.print(x))
  }
}

class StringKeyedMapSerializer extends Serializer[Map[String, Any]] {
  private val Class = classOf[Map[String, Any]]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Map[String, Any]] = {
    // not sure why t
    Map()
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case m: Map[_, _] => JObject(m.map {
      case (k: String, v) => JField(k, Extraction.decompose(v))
    }.toList)
  }
}