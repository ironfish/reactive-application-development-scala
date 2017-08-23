package com.rarebooks.library

import akka.actor.ActorDSL._
import akka.routing.{RoundRobinRoutingLogic, ActorRefRoutee, Router}
import akka.testkit.{ EventFilter, TestProbe }

class RareBooksSpec extends BaseAkkaSpec("as-3002-rare-books-spec") {

  import RareBooksProtocol._

  val nbrOfLibrarians = system.settings.config getInt "rare-books.nbr-of-librarians"

  "Creating RareBooks" should {
    val rareBooks = system.actorOf(RareBooks.props, "rare-books")
    "create nbrOfLibrarians" in {
      for(i <- 0 to nbrOfLibrarians - 1) {
        TestProbe().expectActor(s"/user/rare-books/librarian-$i")
      }
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
      override def createLibrarian(): Router = {
        val routees: Vector[ActorRefRoutee] = Vector.fill(1) {
          val r = librarian.ref
          ActorRefRoutee(r)
        }
        Router(RoundRobinRoutingLogic(), routees)
      }
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
