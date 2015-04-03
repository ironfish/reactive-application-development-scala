package com.rarebooks.library

import scala.concurrent.duration._
import scala.reflect.runtime.universe._
import akka.testkit.TestProbe

class CustomerSpec extends BaseAkkaSpec("as-3001-customer-spec") {

  import Customer._
  import Librarian._

  "Customer messages" should {
    "throw IllegalArgumentException when 'BookFound.books' is empty" in process(typeOf[BookFound])
    "throw IllegalArgumentException when 'BookNotFound.reason' is empty" in process(typeOf[BookNotFound])
  }

  "Receiving a BookFound event to Customer" should {
    "increase Customer.model.bookFound by 1 for 1 book found" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props)
      val customer = system.actorOf(Customer.props(librarian))
      customer ! FindBookByTitle("The Epic of Gilgamesh")
      sender.awaitAssert {
        sender.within(1.second) {
          customer ! GetCustomer()
          sender.expectMsg(Customer.CustomerModel(1, 0))
        }
      }
    }

    "increase Customer.model.bookFound by 2 for 2 books found" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props)
      val customer = system.actorOf(Customer.props(librarian))
      customer ! FindBookByTags(Set("greece"))
      sender.awaitAssert {
        sender.within(1.second) {
          customer ! GetCustomer()
          sender.expectMsg(Customer.CustomerModel(2, 0))
        }
      }
    }
  }

  "Receiving a BookNotFound event to Customer" should {
    "increase Customer.model.bookNotFound by 1 for 1 book not found" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val librarian = system.actorOf(Librarian.props)
      val customer = system.actorOf(Customer.props(librarian))
      customer ! FindBookByTitle("The Batman")
      sender.awaitAssert {
        sender.within(1.second) {
          customer ! GetCustomer()
          sender.expectMsg(Customer.CustomerModel(0, 1))
        }
      }
    }
  }

  private def process(t: Type) = {
    intercept[IllegalArgumentException] {
      t match {
        case found    if t =:= typeOf[BookFound]        => BookFound(List())
        case notFound if t =:= typeOf[BookNotFound]     => BookNotFound("")
      }
    }
  }
}
