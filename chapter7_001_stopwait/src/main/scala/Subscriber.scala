import akka.actor.Actor

object Subscriber {
  case object Register
  case class Work(m: String)
}

import Subscriber.{Register, Work}
class Subscriber extends Actor {
  override def receive = {
    case Register =>
      sender() ! Publisher.Ok
    case Work(m) =>
      System.out.println(s"Working on $m")
      sender() ! Publisher.Ok
  }
}
