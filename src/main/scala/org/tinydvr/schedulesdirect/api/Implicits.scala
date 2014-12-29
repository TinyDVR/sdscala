package org.tinydvr.schedulesdirect.api

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object Implicits {

  implicit val global = scala.concurrent.ExecutionContext.Implicits.global
  implicit val http = dispatch.Http

  implicit def futureToAwaitableFuture[T](f : Future[T])(implicit executor: scala.concurrent.ExecutionContext) : WaitableFuture[T] = {
    new WaitableFuture[T](f)
  }

  class WaitableFuture[T](f: Future[T])(implicit executor: scala.concurrent.ExecutionContext) {
    def getResult(): T = {
      Await.result(f, Duration.Inf)
    }
  }
}
