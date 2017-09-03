package com.rarebooks.library

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import akka.NotUsed

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import LibraryProtocol.BookCard

object Cataloging extends App {
  implicit val system = ActorSystem("catalog-loader")
  implicit val materializer = ActorMaterializer()

  val file = Paths.get("books.csv")

  private val framing: Flow[ByteString, ByteString, NotUsed] =
    Framing.delimiter(ByteString("\n"),
      maximumFrameLength = 256,
      allowTruncation = true)

  private val parsing: ByteString => Array [String] =
    _.utf8String.split(",")

  private val conversion: Array[String] => BookCard =
    s => BookCard(
      isbn = s(0),
      author = s(1),
      title = s(2),
      description = s(3),
      dateOfOrigin = s(4),
      topic = Set.empty,
      publisher = s(6),
      language = s(7),
      pages = s(8).toInt
    )

  val result = FileIO.fromPath(file)
      .via(framing)
      .map(parsing)
      .map(conversion)
      .to(Sink.foreach(println(_)))
      .run()

  Await.ready(result, Duration.Inf)
  system.terminate()
}
