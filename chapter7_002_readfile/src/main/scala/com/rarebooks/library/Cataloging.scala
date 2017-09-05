package com.rarebooks.library

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Cataloging extends App {
  implicit val system = ActorSystem("catalog-loader")
  implicit val materializer = ActorMaterializer()

  val file = Paths.get("books.csv")

  val result = FileIO.fromPath(file)
      .to(Sink.foreach(println(_)))
      .run()

  Await.ready(result, Duration.Inf)
  system.terminate()
}
