import akka.actor.Actor

class Echo extends Actor {
  def receive = {
    case msg =>
      sender() ! msg
  }
}