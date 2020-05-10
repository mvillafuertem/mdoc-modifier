package io.github.mvillafuertem.mdoc.modifier.akka.http.sample

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

final class SampleController {

  val routes: Route =
    get {
      path("health") {
        complete(HttpEntity(ContentTypes.`application/json`, """{"status": "UP"}"""))
      }
    }
}
