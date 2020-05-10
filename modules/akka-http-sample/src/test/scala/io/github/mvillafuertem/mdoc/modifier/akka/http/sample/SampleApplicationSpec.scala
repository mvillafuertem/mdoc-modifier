package io.github.mvillafuertem.mdoc.modifier.akka.http.sample

import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class SampleApplicationSpec extends AnyFlatSpecLike with Matchers with ScalatestRouteTest {

  behavior of "Sample Application"

  it should "health" in {

    // G I V E N
    val controller = new SampleController()

    // W H E N
    Get("/health") ~>
      controller.routes ~>
      check {

        // T H E N
        status shouldBe StatusCodes.OK
        header[`Content-Type`] shouldBe Some(`Content-Type`(`application/json`))

      }

  }

}
