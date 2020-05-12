package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{ BasicDirectives, LoggingMagnet }
import akka.http.scaladsl.server.{ Directive, Directive0, RouteResult }
import akka.http.scaladsl.testkit.RouteTestResultComponent
import akka.http.scaladsl.util.FastFuture
import io.github.mvillafuertem.mdoc.modifier.akka.http.RouteTestInterceptor._

import scala.concurrent.Future

class RouteTestInterceptor extends RouteTestResultComponent {

  def logRequestResult(magnet: LoggingMagnet[HttpRequest => RouteResult => Unit]): Directive0 =
    BasicDirectives.extractRequestContext.flatMap { ctx =>
      val logResult = magnet.f(ctx.log)(ctx.request)
      mapRouteResult { result =>
        logResult(result)
        println(result)
        result match {
          case RouteResult.Complete(response)   =>
            AkkaHttpRestDocs(ctx.request, response).printer()
          case RouteResult.Rejected(rejections) =>
            println(rejections)
        }
        result
      }
    }

  def mapRouteResult(f: RouteResult => RouteResult): Directive0 =
    Directive(inner => ctx => inner(())(ctx).fast.map(f)(ctx.executionContext))

  override def failTest(msg: String): Nothing = ???

}

object RouteTestInterceptor {

  implicit class EnhancedFuture[T](val future: Future[T]) extends AnyVal {
    def fast: FastFuture[T] = new FastFuture[T](future)
  }

}
