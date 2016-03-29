package com.rarebooks.library


import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
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
  createRareBooks()

  /**
   * Create rarebooks factory method.
   *
   * @return rareBooks ActorRef
   */
  protected def createRareBooks(): ActorRef = {
    system.actorOf(RareBooks.props, "rare-books")
  }

  def run(): Unit = {
    log.warning(f"{} running%nWaiting for customer requests.", getClass.getSimpleName)
    commandLoop()
    Await.ready(system.whenTerminated, Duration.Inf)
  }

  @tailrec
  private def commandLoop(): Unit =
    Command(StdIn.readLine()) match {
      case Command.Customer(count, odds, tolerance) =>
        log.warning(s"Enter customer commands from the customer app prompt.")
        commandLoop()
      case Command.Quit =>
        system.terminate()
      case Command.Unknown(command) =>
        log.warning(s"Unknown command $command")
        commandLoop()
    }
}
