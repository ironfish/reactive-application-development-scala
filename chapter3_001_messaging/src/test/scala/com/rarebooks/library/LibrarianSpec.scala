package com.rarebooks.library

import akka.testkit.{ EventFilter, TestProbe }
import scala.reflect.runtime.universe._

class LibrarianSpec extends BaseAkkaSpec("as-3001-librarian-spec") {

  import Customer._
  import Librarian._

  "Librarian messages" should {
    "throw IllegalArgumentException when 'FindBookByIsbn.isbn' is empty" in process(typeOf[FindBookByIsbn])
    "throw IllegalArgumentException when 'FindBookByAuthor.author' is empty" in process(typeOf[FindBookByAuthor])
    "throw IllegalArgumentException when 'FindBookByTitle.title' is empty" in process(typeOf[FindBookByTitle])
    "throw IllegalArgumentException when 'FindBookByTags.tags' is empty" in process(typeOf[FindBookByTags])
  }

  "Sending a message to Librarian" should {
    "if book exists, result in BookFound" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props)
      librarian ! FindBookByTitle("The Epic of Gilgamesh")
      sender.expectMsgType[BookFound]
    }

    "log BookNotFound at info" in {
      EventFilter.info(pattern = ".*BookNotFound\\(Book\\(s\\) not found based on Swiss Family Robinson.*",
          occurrences = 1) intercept {
        val librarian = system.actorOf(Librarian.props)
        librarian ! FindBookByTitle("Swiss Family Robinson")
      }
    }

    "if book does not exist" should {
      "return BookNotFound" in {
        val sender = TestProbe()
        implicit val _ = sender.ref
        val librarian = system.actorOf(Librarian.props)
        librarian ! FindBookByTitle("Swiss Family Robinson")
        sender.expectMsgType[BookNotFound]
      }
    }
  }

  private def process(t: Type) = {
    intercept[IllegalArgumentException] {
      t match {
        case isbn     if t =:= typeOf[FindBookByIsbn]   => FindBookByIsbn("")
        case author   if t =:= typeOf[FindBookByAuthor] => FindBookByAuthor("")
        case title    if t =:= typeOf[FindBookByTitle]  => FindBookByTitle("")
        case tag      if t =:= typeOf[FindBookByTags]   => FindBookByTags(Set())
      }
    }
  }
}
