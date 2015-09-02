package org.tinydvr.schedulesdirect.api

import net.liftweb.json.{JsonParser, MappingException}
import org.joda.time.DateTime
import scala.util.Try

/**
 * The error codes that can be returned by the api, indexed by response.
 * See https://github.com/SchedulesDirect/JSON-Service/wiki/API-20140530#tasks-your-client-must-perform
 */

trait SchedulesDirectErrors {

  import SchedulesDirectResponseCodes._

  val errors = new SchedulesDirectErrors

  class SchedulesDirectErrors extends AsyncHttpHelpers {

    def extract(code: Int, body: String): Option[Exception] = {
      (for {
        response <- Try(fromJson[ErrorResponse](body)).toOption
        toException <- errors.byResponse.get(response.response)
      } yield {
        toException(response.code, response.serverID, response.message, response.datetime)
      })
    }

    val byResponse = Map(
      ACCOUNT_EXPIRED -> AccountExpiredException,
      ACCOUNT_LOCKOUT -> AccountLockoutException,
      DEFLATE_REQUIRED -> DeflateRequiredException,
      DUPLICATE_LINEUP -> DuplicateLineupException,
      HCF -> HcfException,
      IMAGE_NOT_FOUND -> ImageNotFoundException,
      INVALID_COUNTRY -> InvalidCountryException,
      INVALID_HASH -> InvalidHashException,
      INVALID_JSON -> InvalidJsonException,
      INVALID_LINEUP -> InvalidLineupException,
      INVALID_LINEUP_DELETE -> InvalidLineupDeleteException,
      INVALID_PARAMETER_COUNTRY -> InvalidParameterCountryException,
      INVALID_PARAMETER_FETCHTYPE -> InvalidParameterFetchtypeException,
      INVALID_PARAMETER_POSTALCODE -> InvalidParameterPostalcodeException,
      INVALID_PROGRAMID -> InvalidProgramidException,
      INVALID_USER -> InvalidUserException,
      LINEUP_DELETED -> LineupDeletedException,
      LINEUP_NOT_FOUND -> LineupNotFoundException,
      LINEUP_QUEUED -> LineupQueuedException,
      LINEUP_WRONG_FORMAT -> LineupWrongFormatException,
      MAX_LINEUPS -> MaxLineupsException,
      MAX_LINEUP_CHANGES_REACHED -> MaxLineupChangesReachedException,
      NO_LINEUPS -> NoLineupsException,
      PROGRAMID_QUEUED -> ProgramidQueuedException,
      REQUIRED_ACTION_MISSING -> RequiredActionMissingException,
      REQUIRED_PARAMETER_MISSING_COUNTRY -> RequiredParameterMissingCountryException,
      REQUIRED_PARAMETER_MISSING_MSGID -> RequiredParameterMissingMsgidException,
      REQUIRED_PARAMETER_MISSING_POSTALCODE -> RequiredParameterMissingPostalcodeException,
      REQUIRED_REQUEST_MISSING -> RequiredRequestMissingException,
      SCHEDULE_NOT_FOUND -> ScheduleNotFoundException,
      SCHEDULE_QUEUED -> ScheduleQueuedException,
      SCHEDULE_RANGE_EXCEEDED -> ScheduleRangeExceededException,
      SERVICE_OFFLINE -> ServiceOfflineException,
      STATIONID_NOT_FOUND -> StationidNotFoundException,
      TOKEN_MISSING -> TokenMissingException,
      UNKNOWN_LINEUP -> UnknownLineupException,
      UNSUPPORTED_COMMAND -> UnsupportedCommandException
    )
  }

}

object SchedulesDirectResponseCodes {
  val ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED"
  val ACCOUNT_LOCKOUT = "ACCOUNT_LOCKOUT"
  val DEFLATE_REQUIRED = "DEFLATE_REQUIRED"
  val DUPLICATE_LINEUP = "DUPLICATE_LINEUP"
  val HCF = "HCF"
  val IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND"
  val INVALID_COUNTRY = "INVALID_COUNTRY"
  val INVALID_HASH = "INVALID_HASH"
  val INVALID_JSON = "INVALID_JSON"
  val INVALID_LINEUP = "INVALID_LINEUP"
  val INVALID_LINEUP_DELETE = "INVALID_LINEUP_DELETE"
  val INVALID_PARAMETER_COUNTRY = "INVALID_PARAMETER:COUNTRY"
  val INVALID_PARAMETER_FETCHTYPE = "INVALID_PARAMETER:FETCHTYPE"
  val INVALID_PARAMETER_POSTALCODE = "INVALID_PARAMETER:POSTALCODE"
  val INVALID_PROGRAMID = "INVALID_PROGRAMID"
  val INVALID_USER = "INVALID_USER"
  val LINEUP_DELETED = "LINEUP_DELETED"
  val LINEUP_NOT_FOUND = "LINEUP_NOT_FOUND"
  val LINEUP_QUEUED = "LINEUP_QUEUED"
  val LINEUP_WRONG_FORMAT = "LINEUP_WRONG_FORMAT"
  val MAX_LINEUPS = "MAX_LINEUPS"
  val MAX_LINEUP_CHANGES_REACHED = "MAX_LINEUP_CHANGES_REACHED"
  val NO_LINEUPS = "NO_LINEUPS"
  val PROGRAMID_QUEUED = "PROGRAMID_QUEUED"
  val REQUIRED_ACTION_MISSING = "REQUIRED_ACTION_MISSING"
  val REQUIRED_PARAMETER_MISSING_COUNTRY = "REQUIRED_PARAMETER_MISSING:COUNTRY"
  val REQUIRED_PARAMETER_MISSING_MSGID = "REQUIRED_PARAMETER_MISSING:MSGID"
  val REQUIRED_PARAMETER_MISSING_POSTALCODE = "REQUIRED_PARAMETER_MISSING:POSTALCODE"
  val REQUIRED_REQUEST_MISSING = "REQUIRED_REQUEST_MISSING"
  val SCHEDULE_NOT_FOUND = "SCHEDULE_NOT_FOUND"
  val SCHEDULE_QUEUED = "SCHEDULE_QUEUED"
  val SCHEDULE_RANGE_EXCEEDED = "SCHEDULE_RANGE_EXCEEDED"
  val SERVICE_OFFLINE = "SERVICE_OFFLINE"
  val STATIONID_NOT_FOUND = "STATIONID_NOT_FOUND"
  val TOKEN_MISSING = "TOKEN_MISSING"
  val UNKNOWN_LINEUP = "UNKNOWN_LINEUP"
  val UNSUPPORTED_COMMAND = "UNSUPPORTED_COMMAND"
}

//
// An exception representing each error the api can return.
//

case class AccountExpiredException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class AccountLockoutException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class DeflateRequiredException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class DuplicateLineupException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class HcfException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ImageNotFoundException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidCountryException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidHashException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidJsonException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidLineupDeleteException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidLineupException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidParameterCountryException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidParameterFetchtypeException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidParameterPostalcodeException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidProgramidException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class InvalidUserException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class LineupDeletedException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class LineupNotFoundException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class LineupQueuedException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class LineupWrongFormatException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class MaxLineupChangesReachedException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class MaxLineupsException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class NoLineupsException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ProgramidQueuedException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class RequiredActionMissingException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class RequiredParameterMissingCountryException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class RequiredParameterMissingMsgidException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class RequiredParameterMissingPostalcodeException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class RequiredRequestMissingException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ScheduleNotFoundException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ScheduleQueuedException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ScheduleRangeExceededException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class ServiceOfflineException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class StationidNotFoundException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class TokenMissingException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class UnknownLineupException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

case class UnsupportedCommandException(code: Int, serverID: String, message: String, datetime: DateTime) extends Exception(message)

