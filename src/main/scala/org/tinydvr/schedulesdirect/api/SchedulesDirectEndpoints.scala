package org.tinydvr.schedulesdirect.api

trait SchedulesDirectEndpoints {

  val endpoints = new Object {
    import SchedulesDirectEndpoints._
    val base = SD_BASE
    val host = SD_HOST
    val client = SD_BASE + "version/" + SchedulesDirectAPIClient.SD_USER_AGENT
    val headends = SD_BASE + "headends"
    val lineups = SD_BASE + "lineups"
    val programs = SD_BASE + "programs"
    val schedules = SD_BASE + "schedules"
    val schedulesMd5 = SD_BASE + "schedules/md5"
    val status = SD_BASE + "status"
    val token = SD_BASE + "token"
  }

}

private[api] object SchedulesDirectEndpoints {

  val SD_HOST = "https://json.schedulesdirect.org/"
  val SD_API_VERSION = "20141201"
  val SD_BASE = SD_HOST + SD_API_VERSION + "/"

}