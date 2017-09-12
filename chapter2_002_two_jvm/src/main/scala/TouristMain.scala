import java.util.Locale

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout

import Tourist.Start

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.SECONDS
import scala.util.{Failure, Success}

object TouristMain extends App {
  val system: ActorSystem = ActorSystem("TouristSystem")

  val path =
    "akka.tcp://BookSystem@127.0.0.1:2553/user/guidebook"

  implicit val timeout: Timeout = Timeout(5, SECONDS)

  system.actorSelection(path).resolveOne().onComplete {
    case Success(guidebook) =>

      val tourProps: Props =
        Props(classOf[Tourist], guidebook)
      val tourist: ActorRef = system.actorOf(tourProps)

      tourist ! Start(Locale.getISOCountries)

    case Failure(e) => println(e)
  }
}
