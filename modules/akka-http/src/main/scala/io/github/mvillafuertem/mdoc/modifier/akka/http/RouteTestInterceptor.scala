package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.RouteTestResultComponent
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect}


@Aspect
class RouteTestInterceptor extends RouteTestResultComponent {

  @Around("execution (* akka.http.scaladsl.testkit.RouteTest.TildeArrow.apply(..)) && args(request, route)")
  def onBindAndHandle(pjp: ProceedingJoinPoint, request: HttpRequest, route: Route): RouteTestResult = {

    val value: RouteTestResult = pjp.proceed().asInstanceOf[RouteTestResult]
    AkkaHttpRestDocs(request, value.response).printer()
    value

  }

  override def failTest(msg: String): Nothing = ???

}




