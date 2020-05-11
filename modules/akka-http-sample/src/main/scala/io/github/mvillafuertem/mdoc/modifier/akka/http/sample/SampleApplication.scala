package io.github.mvillafuertem.mdoc.modifier.akka.http.sample

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object SampleApplication extends App {

  implicit val actorSystem: ActorSystem             = ActorSystem()
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext   = actorSystem.dispatcher

  val bindingFuture = Http().bindAndHandle(new SampleController().routes, "localhost", 8080)

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())

}