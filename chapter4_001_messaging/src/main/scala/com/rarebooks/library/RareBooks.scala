package com.rarebooks.library

import akka.actor.{ Actor, ActorRef, ActorLogging, Props, Stash }
import scala.concurrent.duration.{ MILLISECONDS => Millis, FiniteDuration, Duration }

object RareBooks {

  case object Close
  case object Open
  case object Report

  def props: Props =
    Props(new RareBooks)
}

class RareBooks extends Actor with ActorLogging with Stash {

  import context.dispatcher
  import RareBooks._
  import RareBooksProtocol._

  private val openDuration: FiniteDuration =
    Duration(context.system.settings.config.getDuration("rare-books.open-duration", Millis), Millis)

  private val closeDuration: FiniteDuration =
    Duration(context.system.settings.config.getDuration("rare-books.close-duration", Millis), Millis)

  private val findBookDuration: FiniteDuration =
    Duration(context.system.settings.config.getDuration("rare-books.librarian.find-book-duration", Millis), Millis)

  private val librarian = createLibrarian()

  var requestsToday: Int = 0
  var totalRequests: Int = 0

  context.system.scheduler.scheduleOnce(openDuration, self, Close)

  /**
   * Set the initial behavior.
   *
   * @return partial function open
   */
  override def receive: Receive = open

  /**
   * Behavior that simulates RareBooks is open.
   *
   * @return partial function for completing the request.
   */
  private def open: Receive = {
    case m: Msg =>
      librarian forward m
      requestsToday += 1
    case Close =>
      context.system.scheduler.scheduleOnce(closeDuration, self, Open)
      log.info("Closing down for the day")
      context.become(closed)
      self ! Report
  }

  /**
   * Behavior that simulates the RareBooks is closed.
   *
   * @return partial function for completing the request.
   */
  private def closed: Receive = {
    case Report =>
      totalRequests += requestsToday
      log.info(s"$requestsToday requests processed today. Total requests processed = $totalRequests")
      requestsToday = 0
    case Open =>
      context.system.scheduler.scheduleOnce(openDuration, self, Close)
      unstashAll()
      log.info("Time to open up!")
      context.become(open)
    case _ =>
      stash()
  }

  /**
   * Create librarian actor.
   *
   * @return librarian ActorRef
   */
  protected def createLibrarian(): ActorRef = {
    context.actorOf(Librarian.props(findBookDuration), "librarian")
  }
}
