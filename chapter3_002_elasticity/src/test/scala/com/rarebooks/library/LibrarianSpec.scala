package com.rarebooks.library

import akka.testkit.{ EventFilter, TestProbe }
import scala.concurrent.duration.{ Duration, MILLISECONDS => Millis }

class LibrarianSpec extends BaseAkkaSpec("as-3002-librarian-spec") {

  import RareBooksProtocol._

  private val findBookDuration =
    Duration(system.settings.config.getDuration("rare-books.librarian.find-book-duration", Millis), Millis)

  "Receiving FindBookByTitle" should {
    "When book exists, result in BookFound" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props(findBookDuration))
      librarian ! FindBookByTitle("The Epic of Gilgamesh")
      sender.expectMsgType[BookFound]
    }
    "When book does not exist, log BookNotFound at info" in {
      EventFilter.info(pattern = ".*BookNotFound\\(Book\\(s\\) not found based on Swiss Family Robinson.*",
        occurrences = 1) intercept {
        val librarian = system.actorOf(Librarian.props(findBookDuration))
        librarian ! FindBookByTitle("Swiss Family Robinson")
      }
    }
    "When book does not exist, return BookNotFound" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props(findBookDuration))
      librarian ! FindBookByTitle("Swiss Family Robinson")
      sender.expectMsgType[BookNotFound]
    }
  }

  "Receiving FindBookByTopic" should {
    "When book exists, result in BookFound" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props(findBookDuration))
      librarian ! FindBookByTopic(Set(Tradition))
      sender.expectMsgType[BookFound]
    }
    "When book does not exist, log BookNotFound at info" in {
      EventFilter.info(pattern = ".*BookNotFound\\(Book\\(s\\) not found based on Set\\(Unknown\\).*",
        occurrences = 1) intercept {
        val librarian = system.actorOf(Librarian.props(findBookDuration))
        librarian ! FindBookByTopic(Set(Unknown))
      }
    }
    "When book does not exist, return BookNotFound" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props(findBookDuration))
      librarian ! FindBookByTopic(Set(Unknown))
      sender.expectMsgType[BookNotFound]
    }
  }

  "Receiving Complain" should {
    "log Credit issued at info" in {
      EventFilter.info(pattern = ".*Credit issued to customer.*", occurrences = 1) intercept {
        val librarian = system.actorOf(Librarian.props(findBookDuration))
        librarian ! Complain()
      }
    }
    "send Credit" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props(findBookDuration))
      librarian ! Complain()
      sender.expectMsgType[Credit]
    }
  }
}
