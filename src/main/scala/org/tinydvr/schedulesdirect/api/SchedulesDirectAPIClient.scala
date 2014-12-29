package org.tinydvr.schedulesdirect.api

import dispatch.{HttpExecutor, Req}
import scala.concurrent._
import org.joda.time.format.DateTimeFormat

class SchedulesDirectAPIClient(token: String)(
  implicit val http: HttpExecutor,
  implicit val execution: ExecutionContext
) extends AsyncHttpHelpers with SchedulesDirectEndpoints with SchedulesDirectErrors {

  import SchedulesDirectAPIClient._

  //
  // API Endpoints.
  //

  def addLineup(uri: String): Future[ChangeLineupResponse] = {
    authenticatedRequest[ChangeLineupResponse](endpoints.base + uri, Map(), _.PUT)
  }

  def deleteLineup(uri: String): Future[ChangeLineupResponse] = {
    authenticatedRequest[ChangeLineupResponse](endpoints.base + uri, Map(), _.DELETE)
  }

  def getClient(): Future[GetClientResponse] = {
    authenticatedRequest[GetClientResponse](endpoints.client)
  }

  def getHeadends(country: String, zipCode: String): Future[Map[String, GetHeadendResponse]] = {
    val params = Map(PARAM_COUNTRY -> country, PARAM_POSTAL_CODE -> zipCode)
    authenticatedRequest[Map[String, GetHeadendResponse]](endpoints.headends, params)
  }

  def getLineup(uri: String): Future[GetLineupResponse] = {
    authenticatedRequest[GetLineupResponse](endpoints.host + uri)
  }

  def getLineups(): Future[GetLineupsResponse] = {
    authenticatedRequest[GetLineupsResponse](endpoints.lineups)
  }

  def getPrograms(ids: Set[String]): Future[List[ProgramResponse]] = {
    batchPostRequests[ProgramResponse](endpoints.programs, ids.grouped(MAX_PROGRAM_IDS).toList)
  }

  def getSchedules(ids: Set[String], days: Int): Future[List[SchedulesResponse]] = {
    val requests = ids.map(SchedulesRequest(_, days)).grouped(MAX_PROGRAM_IDS).toList
    batchPostRequests[SchedulesResponse](endpoints.schedules, requests)
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

  private def batchPostRequests[S](endpoint: String,
                                   bodies: Traversable[AnyRef]
                                   )(implicit mf: scala.reflect.Manifest[S]): Future[List[S]] = {
    val headers =  Map(HEADER_TOKEN -> token, HEADER_ACCEPT_ENCODING -> REQUIRED_ENCODING)
    Future.sequence(
      bodies.map(body => {
        gzippedBatchRequest(endpoint, Some(body), headers).
          map(_.split(LINE_BREAK_REGEX).toList.flatMap(line => {
           fromJson[S](line)
          }))
      })
    ).map(_.flatten.toList)

  }
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
  val REQUIRED_ENCODING = "deflate,gzip" // should be fixed in future versions?

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