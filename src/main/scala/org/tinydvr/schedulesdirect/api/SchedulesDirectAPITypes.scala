package org.tinydvr.schedulesdirect.api

import org.joda.time.{LocalDate, DateTime}

//
// Json types handled by the api
//

case class GetClientResponse(response: String,
                             code: Int,
                             client: String,
                             version: String,
                             serverID: String,
                             datetime: DateTime)

case class GetTokenRequest(username: String, password: String)

case class GetTokenResponse(code: Int, token: String, message: String, serverID: String)


case class ChangeLineupResponse(code: Int,
                                message: String,
                                serverID: String,
                                datetime: String,
                                response: String,
                                changesRemaining: Int)

case class GetHeadendResponse(`type`: String,
                              location: String,
                              lineups: List[HeadendLineup])

case class HeadendLineup(name: String,
                         uri: String)

// TODO: add support for QAM lineups. See https://github.com/SchedulesDirect/JSON-Service/wiki/20140530-QAM-lineup
case class GetLineupResponse(map: List[LineupStationChannel],
                             stations: List[LineupStationInfo],
                             metadata: LineupMetaData)

case class LineupMetaData(lineup: String,
                          modified: String,
                          transport: String)

case class LineupStationInfo(callsign: String,
                             isCommercialFree: Option[Boolean],
                             name: String,
                             broadcastLanguage: String,
                             descriptionLanguage: String,
                             logo: Option[LineupStationLogo],
                             stationID: String)

case class LineupStationLogo(URL: String, height: Int, width: Int, md5: String)

case class LineupStationChannel(stationID: String,
                                channel: Option[String],
                                uhfVhf: Option[Int],
                                atscMajor: Option[Int],
                                atscMinor: Option[Int])


case class GetLineupsResponse(serverID: String,
                              datetime: String,
                              lineups: List[Lineup])

case class Lineup(name: String,
                  `type`: String,
                  location: String,
                  uri: String)


case class GetStatusResponse(account: AccountStatus,
                             lineups: List[LineupStatus],
                             lastDataUpdate: DateTime,
                             notifications: List[String],
                             systemStatus: List[SystemStatus],
                             serverID: String,
                             code: Int)

case class AccountStatus(expires: DateTime,
                         messages: List[String],
                         maxLineups: Int,
                         nextSuggestedConnectTime: DateTime)

case class LineupStatus(ID: String,
                        modified: DateTime,
                        uri: String)

case class SystemStatus(date: DateTime, status: String, details: String)

case class ProgramResponse(programID: String,
                           titles: List[ProgramTitles],
                           episodeTitle150: Option[String],
                           eventDetails: Option[ProgramEventDetails],
                           descriptions: Option[ProgramDescriptions],
                           originalAirDate: Option[LocalDate],
                           genres: List[String],
                           metadata: List[Map[String, ProgramMetaData]],
                           showType: Option[String],
                           contentAdvisory: Option[Map[String, List[String]]],
                           contentRating: List[ProgramContentRation],
                           movie: Option[ProgramMovieData],
                           cast: List[ProgramCast],
                           crew: List[ProgramCast],
                           recommendations: List[ProgramRecommendation],
                           hasImageArtwork: Option[Boolean],
                           md5: String)

case class ProgramRecommendation(programID: String, title120: String)

case class ProgramCast(personId: String,
                       nameId: Option[String],
                       name: String,
                       role: String,
                       characterName: Option[String],
                       billingOrder: String)

case class ProgramMovieData(year: Option[String],
                            duration: Option[Int],
                            qualityRating: Option[MovieQualityRating])

case class MovieQualityRating(ratingsBody: String,
                              rating: String,
                              minRating: Option[String],
                              maxRating: Option[String],
                              increment: Option[String])


case class ProgramMetaData(season: String, episode: Option[String])

case class ProgramEventDetails(subType: String,
                               venue100: Option[String],
                               teams: List[EventTeam])

case class ProgramContentRation(body: String, code: String)

case class EventTeam(name: String, isHome: Option[Boolean], gameDate: Option[LocalDate])

case class ProgramTitles(title120: String)

case class ProgramDescriptions(description100: List[ProgramDescription],
                               description1000: List[ProgramDescription])

case class ProgramDescription(descriptionLanguage: String, description: String)

case class SchedulesRequest(stationID: String, days: Int)

case class SchedulesResponse(stationID: String,
                             programs: List[ProgramSchedule],
                              metadata: ScheduleMetaData)

case class ScheduleMetaData(modified: DateTime, md5: String, startDate: LocalDate, endDate: LocalDate, days: Int)

case class ProgramSchedule(programID: String,
                           md5: String,
                           airDateTime: DateTime,
                           duration: Int,
                           liveTapeDelay: Option[String],
                           `new`: Option[Boolean],
                           audioProperties: List[String],
                           contentRating: List[ProgramContentRation],
                           contentAdvisory: Option[Map[String, List[String]]],
                           syndication: Option[ProgramSyndication],
                           videoProperties: List[String])

case class ProgramSyndication(source: String, `type`: String)

//
// Error handling
//

case class ErrorResponse(response: String,
                         code: Int,
                         serverID: String,
                         message: String,
                         datetime: DateTime)

case class UnknownResponseException(code: Int, response: String) extends Exception
