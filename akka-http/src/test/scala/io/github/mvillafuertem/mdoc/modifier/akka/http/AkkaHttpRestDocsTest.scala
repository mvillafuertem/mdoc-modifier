package io.github.mvillafuertem.mdoc.modifier.akka.http

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import org.scalatest.flatspec.AnyFlatSpec

final class AkkaHttpRestDocsTest extends AnyFlatSpec {


  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  it should "" in  {


    val value: Source[ByteString, NotUsed] = Source[ByteString](List(ByteString("adsfasdfadsf asdfa")))



    value.runWith(Sink.foreach(println))
    value.runWith(Sink.foreach(println))
    value.runWith(Sink.foreach(println))
    value.runWith(Sink.foreach(println))
    value.runWith(Sink.foreach(println))




  }

}
