package org.tinydvr.schedulesdirect.api

import dispatch.{HttpExecutor, Req, url}
import net.liftweb.json._
import org.apache.commons.io.IOUtils
import scala.concurrent.{ExecutionContext, Future}

trait AsyncHttpHelpers {

  import SchedulesDirectAPIClient._

  implicit val formats = DefaultFormats + new DateTimeSerializer + new LocalDateSerializer

  protected def httpRequest[S](endpoint: String,
                               toError: (Int, String) => Option[Exception],
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
    val isCompressed = headers.get(HEADER_ACCEPT_ENCODING) == Some(GZIP_ENCODING)
    val request = body.map(Serialization.write(_)).map(initial << _).getOrElse(initial)
    http(request > (rs => {
      val body =
        if (isCompressed) {
          val stream = new java.util.zip.GZIPInputStream(rs.getResponseBodyAsStream)
          IOUtils.toString(stream)
        } else {
          rs.getResponseBody
        }
      try {
        fromJson(body)
      } catch {
        case e: MappingException => {
          throw toError(rs.getStatusCode, body).getOrElse(e)
        }
      }
    }))
  }

  protected def fromJson[T](is: String)(implicit mf: scala.reflect.Manifest[T]): T = {
    JsonParser.parse(is).extract[T]
  }

}
