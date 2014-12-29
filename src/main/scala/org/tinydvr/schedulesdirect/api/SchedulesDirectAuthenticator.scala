package org.tinydvr.schedulesdirect.api

import dispatch.HttpExecutor
import scala.concurrent.{Future, ExecutionContext}
import org.apache.commons.codec.digest.DigestUtils

object SchedulesDirectAuthenticator extends AsyncHttpHelpers with SchedulesDirectEndpoints with SchedulesDirectErrors {

  def getToken(username: String, password: String)(implicit http: HttpExecutor,
                                                   execution: ExecutionContext): Future[GetTokenResponse] = {
    val req = GetTokenRequest(username, DigestUtils.shaHex(password))
    httpRequest[GetTokenResponse](endpoints.token, errors.extract, Some(req))
  }
}
