package org.tinydvr.schedulesdirect.api

import dispatch.{HttpExecutor, Req, url}
import net.liftweb.json.{DefaultFormats, JsonParser, MappingException, Serialization}
import org.apache.commons.io.IOUtils
import scala.concurrent.{ExecutionContext, Future}

trait AsyncHttpHelpers {

  import SchedulesDirectAPIClient._

  implicit val formats =
    DefaultFormats +
      new DateTimeSerializer +
      new LocalDateSerializer

  protected def httpRequest[S](endpoint: String,
                               toError: (Int, String) => Exception,
                               body: Option[AnyRef] = None,
                               params: Map[String, String] = Map.empty[String, String],
                               transformations: (Req) => Req = (r: Req) => r,
                               headers: Map[String, String] = Map.empty[String, String]
                                )(implicit
                                  http: HttpExecutor,
                                  execution: ExecutionContext,
                                  mf: scala.reflect.Manifest[S]
                                ): Future[S] = {
    val initial = transformations(
      url(endpoint).setContentType(HTTP_CONTENT_TYPE_JSON, CHARSET) <:<
        (SD_DEFAULT_HEADERS ++ headers) <<? params
    )
    val request = body.map(Serialization.write(_)).map(initial << _).getOrElse(initial)
    http(request > (rs => {
      val body = rs.getResponseBody
      fromJson[S](body).getOrElse(throw toError(rs.getStatusCode, body))
    }))
  }

  protected def gzippedBatchRequest(endpoint: String,
                                    body: Option[AnyRef] = None,
                                    headers: Map[String, String] = Map.empty[String, String]
                                     )(implicit
                                       http: HttpExecutor,
                                       execution: ExecutionContext
                                     ): Future[String] = {
    val initial =
      url(endpoint).setContentType(HTTP_CONTENT_TYPE_JSON, CHARSET) <:<
        (SD_DEFAULT_HEADERS ++ headers)

    val request = body.map(Serialization.write(_)).map(initial << _).getOrElse(initial)
    http(request > (rs => {
      val stream = new java.util.zip.GZIPInputStream(rs.getResponseBodyAsStream)
      IOUtils.toString(stream)
    }))
  }

  protected def fromJson[T](is: String)(implicit mf: scala.reflect.Manifest[T]): Option[T] = {
    try {
      val json = JsonParser.parse(is)
      Some(json.extract[T])
    } catch {
      case e: MappingException => {
        None
      }
    }

  }

}
