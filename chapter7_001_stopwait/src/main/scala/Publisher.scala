import akka.actor.{Actor, ActorRef}
import Subscriber.{Register, Work}

object Publisher {
  case object Ok
}

class Publisher(subscriber: ActorRef) extends Actor {
  override def preStart =
    subscriber ! Register
  override def receive = {
    case Publisher.Ok =>
      subscriber ! Work("Do something!")
  }  
}

