package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.http.scaladsl.model._
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.collection.immutable

class AkkaHttpRestDocs(request: HttpRequest, response: HttpResponse) {

  def printer(headingLevel: String = "##"): Unit = {

    val title: String = s"${request.method.value} ${request.uri}"

    // R E Q U E S T
    val requestHeader: immutable.Seq[String] = request.headers.map(a => a.value())
    val requestEntity: String                = s"${request.entity}"

    // R E S P O N S E
    val status: String                        = s"${response.status}"
    val protocol: String                      = s"${response.protocol.value}"
    val responseHeader: immutable.Seq[String] = response.headers.map(a => a.value())
    val responseEntity: ResponseEntity        = response.entity

    val bytes: Source[ByteString, Any] = responseEntity.dataBytes

    val pepe = s"${responseEntity}"

    //val value = responseEntity.getDataBytes().toMat()

    //val output = Sink.fold[ByteString, String]()((count, _) => count + "")

    println(s"""
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
               |$pepe
               |
               |```
       """.stripMargin)

  }
}
