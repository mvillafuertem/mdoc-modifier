package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.RouteTestResultComponent
import akka.stream.{ActorMaterializer, Materializer}
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}


@Aspect
class RouteTestInterceptor extends RouteTestResultComponent {

  @Pointcut("execution (* akka.http.scaladsl.testkit.RouteTest.TildeArrow.apply(..)) && args(request, route)")
  def tildeArrowApply(request: HttpRequest, route: Route): Unit = ()

  @Pointcut("execution (* akka.http.scaladsl.testkit.RouteTest.TildeArrow..injectIntoRoute(..)) && args( *, *, *, *, materializer, ..)")
  def tildeArrowMaterializer(materializer: Materializer): Unit = ()


  @Around("tildeArrowApply(request, route)")
  def onBindAndHandle(pjp: ProceedingJoinPoint, request: HttpRequest, route: Route): RouteTestResult = {

    val instance = pjp.getThis
    val privateUriField = instance.getClass.getDeclaredField("materializer")
    privateUriField.setAccessible(true)

    val privateUri = privateUriField.get(instance).asInstanceOf[ActorMaterializer]

    println(privateUri)
    val value: RouteTestResult = pjp.proceed().asInstanceOf[RouteTestResult]
    println("MATERIALIZER")
    //println(materializer.toString)
    //AkkaHttpRestDocs(request, value.response).printer()
    value

  }


//  @Before("execution (* akka.http.scaladsl.testkit.RouteTest.TildeArrow.*.*(..))")
//  def beforeTraceMethods(joinPoint: JoinPoint): Unit = {
//
//    println(joinPoint.getArgs)
//    println(joinPoint.getKind)
//    println(joinPoint.getTarget)
//    println(joinPoint.getSignature.getName)
//    println(joinPoint.getSignature.getDeclaringTypeName)
//    println(joinPoint.getSignature.getDeclaringType)
//    println(joinPoint.getSignature)
//    println("++++++++++++++++++++++")
//
//  }

  // ++++++++++++++++++++++
//  @Around("within (* akka.http.scaladsl.testkit.RouteTest.(..)) && args( *, *, *, *, materializer, ..)")
//  def onBindAndHandleb(pjp: ProceedingJoinPoint, materializer: Materializer): Any = {
//
//
//    println("MATERIALIZER")
//    println("MATERIALIZER")
//    println("MATERIALIZER")
//    println("MATERIALIZER")
//    println("MATERIALIZER")
//    println("MATERIALIZER")
//    println(materializer.toString)
//
//    println("----------------------")
//    println("----------------------")
//    println("----------------------")
//    println("----------------------")
//    println("----------------------")
//
//    pjp.proceed()
//
//  }

  override def failTest(msg: String): Nothing = ???
}




