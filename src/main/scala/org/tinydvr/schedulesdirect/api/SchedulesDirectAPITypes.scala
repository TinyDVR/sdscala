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
                                datetime: DateTime,
                                response: String,
                                changesRemaining: Int)

case class GetHeadendResponse(headend: String,
                              transport: String,
                              location: String,
                              lineups: List[HeadendLineup])

case class HeadendLineup(name: String,
                         lineup: String,
                         uri: String)

// TODO: add support for non-Antenna lineups. See "StationID / channel mapping for a lineup"
// on https://github.com/SchedulesDirect/JSON-Service/wiki/API-20141201
case class GetLineupResponse(map: List[AntennaLineupStationChannel],
                             stations: List[LineupStationInfo],
                             metadata: LineupMetaData)

case class LineupMetaData(lineup: String,
                          modified: String,
                          transport: String)

case class LineupStationInfo(callsign: String,
                             isCommercialFree: Option[Boolean],
                             name: String,
                             broadcastLanguage: List[String],
                             descriptionLanguage: List[String],
                             broadcaster: Option[LineupStationBroadcaster],
                             logo: Option[LineupStationLogo],
                             stationID: String)

case class LineupStationBroadcaster(city: String, state: String, postalcode: String, country: String)

case class LineupStationLogo(URL: String, height: Int, width: Int, md5: String)

case class AntennaLineupStationChannel(stationID: String,
                                       uhfVhf: Int,
                                       atscMajor: Option[Int],
                                       atscMinor: Option[Int])


case class GetLineupsResponse(code: String,
                              serverID: String,
                              datetime: DateTime,
                              lineups: List[Lineup])

case class Lineup(lineup: String,
                  name: String,
                  transport: String,
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
                         maxLineups: Int)

case class LineupStatus(lineup: String,
                        modified: DateTime,
                        uri: String)

case class SystemStatus(date: DateTime, status: String, message: String)

case class ProgramResponse(programID: String,
                           titles: List[ProgramTitles],
                           eventDetails: Option[ProgramEventDetails],
                           descriptions: Option[ProgramDescriptions],
                           originalAirDate: Option[LocalDate],
                           genres: List[String],
                           episodeTitle150: Option[String],
                           metadata: List[Map[String, ProgramMetaData]],
                           showType: Option[String],
                           hasImageArtwork: Option[Boolean],
                           md5: String,
                           cast: List[ProgramCast],
                           crew: List[ProgramCast],
                           recommendations: List[ProgramRecommendation])
//                           contentAdvisory: Option[Map[String, List[String]]],
//                           contentRating: List[ProgramContentRation],
//
//                           movie: Option[ProgramMovieData],
//                           )

case class ProgramRecommendation(programID: String, title120: String)

case class ProgramCast(personId: Option[String],
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

case class SchedulesRequest(stationID: String, date: List[String])

case class SchedulesResponse(stationID: String,
                             programs: List[ProgramSchedule],
                             metadata: ScheduleMetaData)

case class ScheduleMetaData(modified: DateTime, md5: String, startDate: LocalDate)

case class ScheduleDateMd5(code: Int, message: String, lastModified: DateTime, md5: String)

case class ProgramSchedule(programID: String,
                           md5: String,
                           airDateTime: DateTime,
                           duration: Int,
                           audioProperties: List[String],
                           videoProperties: List[String],
                           // Optional fields. TODO: expand to full set?
                           isPremiereOrFinale: Option[String],
                           liveTapeDelay: Option[String],
                           `new`: Option[Boolean])

case class ProgramSyndication(source: String, `type`: String)

//
// Error handling
//

case class ErrorResponse(response: String,
                         code: Int,
                         serverID: String,
                         message: String,
                         datetime: DateTime)

case class UnknownResponseException(code: Int, response: String) extends Exception(response)
