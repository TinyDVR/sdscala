package org.tinydvr.schedulesdirect.api

import dispatch.{HttpExecutor, Req}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import scala.concurrent._

class SchedulesDirectAPIClient(token: String)(
  implicit val http: HttpExecutor,
  implicit val execution: ExecutionContext
) extends AsyncHttpHelpers with SchedulesDirectEndpoints with SchedulesDirectErrors {

  import SchedulesDirectAPIClient._

  //
  // API Endpoints.
  //

  def addLineup(uri: String): Future[ChangeLineupResponse] = {
    authenticatedRequest[ChangeLineupResponse](endpoints.host + uri, Map(), _.PUT)
  }

  def deleteLineup(uri: String): Future[ChangeLineupResponse] = {
    authenticatedRequest[ChangeLineupResponse](endpoints.host + uri, Map(), _.DELETE)
  }

  def getClient(): Future[GetClientResponse] = {
    authenticatedRequest[GetClientResponse](endpoints.client)
  }

  def getHeadends(country: String, zipCode: String): Future[List[GetHeadendResponse]] = {
    val params = Map(PARAM_COUNTRY -> country, PARAM_POSTAL_CODE -> zipCode)
    authenticatedRequest[List[GetHeadendResponse]](endpoints.headends, params)
  }

  def getLineup(uri: String): Future[GetLineupResponse] = {
    authenticatedRequest[GetLineupResponse](endpoints.host + uri)
  }

  def getLineups(): Future[GetLineupsResponse] = {
    authenticatedRequest[GetLineupsResponse](endpoints.lineups)
  }

  def getPrograms(ids: Set[String]): Future[List[ProgramResponse]] = {
    val headers = Map(HEADER_ACCEPT_ENCODING -> GZIP_ENCODING)
    def getPrograms(ids: Iterable[String]) =
      authenticatedRequest[List[ProgramResponse]](endpoints.programs, Map(), r => r, headers, Some(ids))
    batchRequests(ids, MAX_PROGRAM_IDS, getPrograms).map(_.toList)
  }

  def getSchedules(stationIds: Set[String], days: Iterable[LocalDate]): Future[List[SchedulesResponse]] = {
    val headers = Map(HEADER_ACCEPT_ENCODING -> GZIP_ENCODING)
    val dates = days.map(_.toString(LOCAL_DATE_FORMAT)).toList
    val scheduleRequests = stationIds.map(SchedulesRequest(_, dates))
    def getSchedules(ids: Iterable[SchedulesRequest]) =
      authenticatedRequest[List[SchedulesResponse]](endpoints.schedules, Map(), r => r, headers, Some(ids))
    batchRequests(scheduleRequests, MAX_PROGRAM_IDS, getSchedules).map(_.toList)
  }

  def getSchedulesMd5(stationIds: Set[String], days: Iterable[LocalDate]): Future[Map[String, Map[LocalDate, ScheduleDateMd5]]] = {
    val headers = Map(HEADER_ACCEPT_ENCODING -> GZIP_ENCODING)
    val dates = days.map(_.toString(LOCAL_DATE_FORMAT)).toList
    val scheduleRequests = stationIds.map(SchedulesRequest(_, dates))
    def getSchedules(ids: Iterable[SchedulesRequest]) =
      authenticatedRequest[Map[String, Map[String, ScheduleDateMd5]]](endpoints.schedulesMd5, Map(), r => r, headers, Some(ids))
    batchRequests(scheduleRequests, MAX_PROGRAM_IDS, getSchedules).map(_.toMap.mapValues(byDate => {
      byDate.map {
        case (dateString, md5) => (localDateFormatter.parseLocalDate(dateString), md5)
      }
    }))
  }

  def getStatus(): Future[GetStatusResponse] = {
    authenticatedRequest[GetStatusResponse](endpoints.status)
  }

  //
  // Private helpers
  //

  private def authenticatedRequest[S](endpoint: String,
                                      params: Map[String, String] = Map.empty[String, String],
                                      transformations: (Req) => Req = (r: Req) => r,
                                      headers: Map[String, String] = Map.empty[String, String],
                                      body: Option[AnyRef] = None
                                       )(implicit mf: scala.reflect.Manifest[S]): Future[S] = {
    httpRequest(endpoint, errors.extract, body, params, transformations, headers + (HEADER_TOKEN -> token))
  }

  private def batchRequests[T, S](bodies: Iterable[T], groupSize: Int, f: Iterable[T] => Future[Iterable[S]]): Future[Iterable[S]] = {
    Future.sequence(
      bodies.grouped(groupSize).map(f).toList
    )
  }.map(_.flatten)

}

/**
 * Some constants used by the api.
 */
private[api] object SchedulesDirectAPIClient {

  // all requests should have a user agent https://github.com/SchedulesDirect/JSON-Service/wiki/API-20140530
  val SD_USER_AGENT = "sdscala-grabber"

  //
  // Limits enforced by the server
  //

  val MAX_PROGRAM_IDS = 5000 // https://github.com/SchedulesDirect/JSON-Service/wiki/API-20140530
  val GZIP_ENCODING = "deflate,gzip" // should be fixed in future versions?

  //
  // Formats
  //

  val CHARSET = "UTF-8"
  val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
  val LOCAL_DATE_FORMAT = "yyyy-MM-dd"
  val dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT)
  val localDateFormatter = DateTimeFormat.forPattern(LOCAL_DATE_FORMAT)

  //
  // Http stuff
  //

  val HEADER_TOKEN = "token"
  val HEADER_USER_AGENT = "User-Agent"
  val HEADER_ACCEPT_ENCODING = "Accept-Encoding"
  val LINE_BREAK_REGEX = """\r?\n"""

  val HTTP_CONTENT_TYPE_JSON: String = "application/json"

  val SD_DEFAULT_HEADERS = Map(
    HEADER_USER_AGENT -> SD_USER_AGENT
  )

  //
  // Parameters
  //

  val PARAM_COUNTRY = "country"
  val PARAM_POSTAL_CODE = "postalcode"

}