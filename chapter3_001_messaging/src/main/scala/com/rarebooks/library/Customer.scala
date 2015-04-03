package com.rarebooks.library

import akka.actor.{ ActorRef, Actor, ActorLogging, Props }
import scala.compat.Platform

object Customer {

  import Catalog._
  import LibraryProtocol._

  final case class GetCustomer(dateInMillis: Long = Platform.currentTime) extends Msg

  final case class BookFound(books: List[BookCard], dateInMillis: Long = Platform.currentTime) extends Evt {
    require(books.nonEmpty, "Book(s) required.")
  }

  final case class BookNotFound(reason: String, dateInMillis: Long = Platform.currentTime) extends Evt {
    require(reason.nonEmpty, "Reason is required.")
  }

  def props(librarian: ActorRef): Props =
    Props(new Customer(librarian))

  /**
   * Customer model companion object.
   */
  object CustomerModel {
    val empty = CustomerModel(0, 0)
  }

  /**
   * Customer model.
   *
   * @param found the number of books found
   * @param notFound the number of books not found
   */
  case class CustomerModel(found: Int, notFound: Int)

  /**
   * Immutable state structure for customer model.
   *
   * @param model updated customer model
   * @param timeInMillis current time in milliseconds
   */
  private case class State(model: CustomerModel, timeInMillis: Long) {
    def updated(r: Evt): State = r match {
      case BookFound(b, d)    => copy(model.copy(found = model.found + b.size), timeInMillis = d)
      case BookNotFound(_, d) => copy(model.copy(notFound = model.notFound + 1), timeInMillis = d)
    }
  }
}

/**
 * Customer actor.
 *
 * @param librarian reference to librarian actor
 */
class Customer(librarian: ActorRef) extends Actor with ActorLogging {

  import Customer._
  import LibraryProtocol._

  private var state = State(CustomerModel.empty, -1L)

  override def receive: Receive = {
    case g: GetCustomer => sender ! state.model
    case c: Cmd         => librarian ! c
    case e: Evt         => e match {
      case br: BookFound    => state = state.updated(br)
      case br: BookNotFound => state = state.updated(br)
    }
  }
}
