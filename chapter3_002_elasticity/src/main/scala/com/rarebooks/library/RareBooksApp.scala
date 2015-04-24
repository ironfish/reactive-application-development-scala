package com.rarebooks.library

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.routing.FromConfig
import scala.annotation.tailrec
import scala.io.StdIn

object RareBooksApp {

  /**
   * Main loop for running program.
   *
   * @param args input arguments
   */
  def main(args: Array[String]): Unit = {
    val system: ActorSystem = ActorSystem("rare-books-system")
    val rareBooksApp: RareBooksApp = new RareBooksApp(system)
    rareBooksApp.run()
  }
}

/**
 * RareBooks bootstrap application.
 *
 * @param system Actor system
 */
class RareBooksApp(system: ActorSystem) extends Console {

  private val log = Logging(system, getClass.getName)
  private val rareBooks = createRareBooks()

  /**
   * Create rarebooks factory method.
   *
   * @return rareBooks ActorRef
   */
  protected def createRareBooks(): ActorRef = {
    system.actorOf(RareBooks.props, "rare-books")
  }

  def run(): Unit = {
    log.warning(f"{} running%nEnter commands [`q` = quit, `2c` = 2 customers, etc.]:", getClass.getSimpleName)
    commandLoop()
    system.awaitTermination()
  }

  @tailrec
  private def commandLoop(): Unit =
    Command(StdIn.readLine()) match {
      case Command.Customer(count, odds, tolerance) =>
        createCustomer(count, odds, tolerance)
        commandLoop()
      case Command.Quit =>
        system.shutdown()
      case Command.Unknown(command) =>
        log.warning(s"Unknown command $command")
        commandLoop()
    }

  /**
   * Create customer factory method.
   *
   * @param count number of customers
   * @param odds chances customer will select a valid topic
   * @param tolerance maximum number of books not found before customer complains
   */
  protected def createCustomer(count: Int, odds: Int, tolerance: Int): Unit =
    for (_ <- 1 to count)
      system.actorOf(Customer.props(rareBooks, odds, tolerance))
}
