package com.rarebooks.library

import akka.actor.{ Actor, ActorLogging, Props }
import scala.compat.Platform

object Librarian {

  import Catalog._
  import Customer._
  import LibraryProtocol._

  final case class FindBookByIsbn(isbn: String, dateInMillis: Long = Platform.currentTime) extends Cmd {
    require(isbn.nonEmpty, "Isbn required.")
  }

  final case class FindBookByAuthor(author: String, dateInMillis: Long = Platform.currentTime) extends Cmd {
    require(author.nonEmpty, "Author required.")
  }

  final case class FindBookByTitle(title: String, dateInMillis: Long = Platform.currentTime) extends Cmd {
    require(title.nonEmpty, "Title required.")
  }

  final case class FindBookByTags(tags: Set[String], dateInMillis: Long = Platform.currentTime) extends Cmd {
    require(tags.nonEmpty, "Tag(s) required.")
  }

  def props: Props = Props(new Librarian)

  private def optToEither[T](v: T, f: T => Option[List[BookCard]]): Either[BookNotFound, BookFound] =
    f(v) match {
      case b: Some[List[BookCard]] => Right(BookFound(b.get))
      case _                       => Left(BookNotFound(s"Book(s) not found based on $v"))
    }

  private def findByIsbn(fb: FindBookByIsbn) =
    optToEither[String](fb.isbn, findBookByIsbn)

  private def findByAuthor(fb: FindBookByAuthor) =
    optToEither[String](fb.author, findBookByAuthor)

  private def findByTitle(fb: FindBookByTitle) =
    optToEither[String](fb.title, findBookByTitle)

  private def findByTags(fb: FindBookByTags) =
    optToEither[Set[String]](fb.tags, findBookByTag)
}

class Librarian extends Actor with ActorLogging {

  import Customer._
  import Librarian._
  import LibraryProtocol._

  override def receive: Receive = {
    case c: Cmd => c match {
      case fb: FindBookByIsbn   => process(findByIsbn(fb))
      case fb: FindBookByAuthor => process(findByAuthor(fb))
      case fb: FindBookByTitle  => process(findByTitle(fb))
      case fb: FindBookByTags   => process(findByTags(fb))
    }
  }

  private def process(r: Either[BookNotFound, BookFound]): Unit = {
    r fold (
      f => {
        sender ! f
        log.info(f.toString)
      },
      s => sender ! s)
  }
}
