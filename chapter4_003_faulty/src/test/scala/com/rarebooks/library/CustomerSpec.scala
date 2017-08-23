package com.rarebooks.library

import scala.concurrent.duration._
import akka.testkit.{EventFilter, TestProbe}

class CustomerSpec extends BaseAkkaSpec("as-3003-customer-spec") {

  import Catalog._
  import RareBooksProtocol._

  private val OddsZero: Int = 0
  private val OddsOneHundred: Int = 100
  private val ToleranceZero: Int = 0
  private val ToleranceFive: Int = 5

  "Receiving BookFound" should {
    "log BookFound at info" in {
      EventFilter.info(pattern = ".*\\d Book\\(s\\) found!.*", occurrences = 1) intercept {
        val customer = system.actorOf(Customer.props(system.deadLetters, OddsOneHundred,  ToleranceFive))
        customer ! BookFound(findBookByIsbn(theEpicOfGilgamesh.isbn).get)
      }
    }
    "increase Customer.model.bookFound by 1 for 1 book found" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsOneHundred, ToleranceFive))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookFound(findBookByIsbn(theEpicOfGilgamesh.isbn).get)
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsOneHundred, ToleranceFive, 1, 0))
        }
      }
    }
    "increase Customer.model.bookFound by 2 for 2 books found" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsOneHundred, ToleranceFive))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookFound(findBookByTopic(Set(Greece)).get)
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsOneHundred, ToleranceFive, 2, 0))
        }
      }
    }
  }

  "Receiving BookNotFound with not found count less than tolerance" should {
    "log number of Books not found! My tolerance is 5" in {
      EventFilter.info(pattern = ".*1 Book\\(s\\) not found! My tolerance is 5.*",
        occurrences = 1) intercept {
        val customer = system.actorOf(Customer.props(system.deadLetters, OddsZero, ToleranceFive))
        customer ! BookNotFound("Unknown")
      }
    }
    "increase Customer.model.bookNotFound by 1 for 1 book not found" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceFive))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsZero, ToleranceFive, 0, 1))
        }
      }
    }
    "send FindBookByTopic message" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceFive))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.expectMsgType[FindBookByTopic]
    }
  }

  "Receiving BookNotFound with not found count greater than tolerance" should {
    "log number of Book(s) not found! Reached my tolerance of 0. Sent complaint! at info" in {
      EventFilter.info(pattern = ".*1 Book\\(s\\) not found! Reached my tolerance of 0. Sent complaint!.*",
          occurrences = 1) intercept {
        val customer = system.actorOf(Customer.props(system.deadLetters, OddsZero, ToleranceZero))
        customer ! BookNotFound("Unknown")
      }
    }
    "increase Customer.model.bookNotFound by 1 for 1 book not found" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceZero))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsZero, ToleranceZero, 0, 1))
        }
      }
    }
    "send a Complain message" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceZero))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.expectMsgType[Complain]
    }
    "stop requesting book info" in {
      val sender = TestProbe()
      val rareBooks = TestProbe()
      implicit val _ = sender.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceZero))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      sender.expectMsgType[Complain]
      rareBooks.expectNoMsg()
    }
  }

  "Receiving Credit" should {
    "log Credit received, will start requesting again!" in {
      EventFilter.info(pattern = ".*Credit received, will start requesting again!.*", occurrences = 1) intercept {
        val customer = system.actorOf(Customer.props(system.deadLetters, OddsOneHundred, ToleranceFive))
        customer ! Credit()
      }
    }
    "reset notFound to 0" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceZero))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsZero, ToleranceZero, 0, 1))
        }
      }
      customer ! Credit()
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsZero, ToleranceZero, 0, 0))
        }
      }
    }
    "request book info" in {
      val rareBooks = TestProbe()
      implicit val _ = rareBooks.ref
      val customer = system.actorOf(Customer.props(rareBooks.ref, OddsZero, ToleranceZero))
      rareBooks.expectMsgType[FindBookByTopic]
      customer ! BookNotFound("Unknown")
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          customer ! GetCustomer()
          rareBooks.expectMsg(Customer.CustomerModel(OddsZero, ToleranceZero, 0, 1))
        }
      }
      customer ! Credit()
      rareBooks.awaitAssert {
        rareBooks.within(1.second) {
          rareBooks.expectMsgType[FindBookByTopic]
        }
      }
    }
  }
}
