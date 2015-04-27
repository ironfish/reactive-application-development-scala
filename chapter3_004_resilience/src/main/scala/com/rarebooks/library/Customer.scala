package com.rarebooks.library

import akka.actor.{ ActorRef, Actor, ActorLogging, Props }
import scala.util.Random

object Customer {

  import RareBooksProtocol._

  def props(rareBooks: ActorRef, odds: Int, tolerance: Int): Props =
    Props(new Customer(rareBooks, odds, tolerance))

  /**
   * Customer model.
   *
   * @param odds the customer's odds of finding a book
   * @param tolerance the customer's tolerance for BookNotFound
   * @param found the number of books found
   * @param notFound the number of books not found
   */
  case class CustomerModel(
    odds: Int,
    tolerance: Int,
    found: Int,
    notFound: Int)

  /**
   * Immutable state structure for customer model.
   *
   * @param model updated customer model
   * @param timeInMillis current time in milliseconds
   */
  private case class State(model: CustomerModel, timeInMillis: Long) {
    def update(m: Msg): State = m match {
      case BookFound(b, d)    => copy(model.copy(found = model.found + b.size), timeInMillis = d)
      case BookNotFound(_, d) => copy(model.copy(notFound = model.notFound + 1), timeInMillis = d)
      case Credit(d)          => copy(model.copy(notFound = 0), timeInMillis = d)
    }
  }
}

/**
 * Customer actor.
 *
 * @param rareBooks reference to librarian actor
 */
class Customer(rareBooks: ActorRef, odds: Int, tolerance: Int) extends Actor with ActorLogging {

  import Customer._
  import RareBooksProtocol._

  // initialize the customer state
  private var state = State(CustomerModel(odds, tolerance, 0, 0), -1L)

  // bootstrap customer requests
  requestBookInfo()

  override def receive: Receive = {
    case m: Msg => m match {
      case f: BookFound =>
        state = state.update(f)
        log.info(f"{} Book(s) found!", f.books.size)
        requestBookInfo()
      case f: BookNotFound if state.model.notFound < state.model.tolerance =>
        state = state.update(f)
        log.info(f"{} Book(s) not found! My tolerance is {}.", state.model.notFound, state.model.tolerance)
        requestBookInfo()
      case f: BookNotFound =>
        state = state.update(f)
        sender ! Complain()
        log.info(f"{} Book(s) not found! Reached my tolerance of {}. Sent complaint!",
          state.model.notFound, state.model.tolerance)
      case c: Credit =>
        state = state.update(c)
        log.info("Credit received, will start requesting again!")
        requestBookInfo()
      case g: GetCustomer =>
        sender ! state.model
    }
  }

  /**
   * Method for requesting book information by topic.
   */
  private def requestBookInfo(): Unit =
    rareBooks ! FindBookByTopic(Set(pickTopic))

  /**
   * Simulate customer picking topic to request. Based on the odds they will randomly pick a viable topic, otherwise
   * they will request Unknown to represent a topic that does not exist.
   *
   * @return topic for book information request
   */
  private def pickTopic: Topic =
    if (Random.nextInt(100) < state.model.odds) viableTopics(Random.nextInt(viableTopics.size)) else Unknown
}
