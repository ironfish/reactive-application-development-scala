package com.rarebooks.library

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }

class RareBooksSpec extends BaseAkkaSpec("as-3001-rare-books-spec") {

  import RareBooksProtocol._

  "Creating RareBooks" should {
    val rareBooks = system.actorOf(RareBooks.props, "rare-books")
    "create child actor librarian" in {
      TestProbe().expectActor("/user/rare-books/librarian")
    }
    "when closed with no requests processed, log `0 requests processed.` at info" in {
      EventFilter.info(pattern = ".*0 requests processed.*", occurrences = 1) intercept {
        rareBooks ! RareBooks.Close
      }
    }
    "when opened, log `Time to open up!` at info" in {
      EventFilter.info(pattern = ".*Time to open up!.*", occurrences = 1) intercept {
        rareBooks ! RareBooks.Open
      }
    }
  }

  "Sending FindBookByTag" should {
    val librarian = TestProbe()
    val rareBooks = actor(new RareBooks() {
      override def createLibrarian() = librarian.ref
    })
    "forward to librarian" in {
      val msg = FindBookByTopic(Set(Greece))
      rareBooks ! msg
      librarian.expectMsg(msg)
    }
    "when closed with one request processed, log `1 requests processed.` at info" in {
      EventFilter.info(pattern = ".*1 requests processed.*", occurrences = 1) intercept {
        rareBooks ! RareBooks.Close
      }
    }
  }
}
