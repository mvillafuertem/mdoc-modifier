package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.http.scaladsl.model._

import scala.collection.immutable


case class AkkaHttpRestDocs(request: HttpRequest, response: HttpResponse) {


  def printer(headingLevel: String = "##"): Unit = {

    val title: String = s"${request.method.value} / ${request.uri}"
    val requestHeader: immutable.Seq[String] = request.headers.map(a => a.value())
    val requestEntity: String = s"${request.entity}"


    val status: String = s"${response.status}"
    val protocol: String = s"${response.protocol.value}"
    val responseHeader: immutable.Seq[String] = response.headers.map(a => a.value())
    val responseEntity: String = s"${response.entity}"

    println(
      s"""
         |$headingLevel `$title`
         |
         |$requestHeader
         |
         |
         |```
         |
         |$requestEntity
         |
         |```
         |
         |#$headingLevel Response
         |
         |$protocol $status
         |
         |$responseHeader
         |
         |```
         |
         |$responseEntity
         |
         |```
       """.stripMargin)

  }
}
