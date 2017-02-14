import akka.actor.{ Actor, ActorSelection }
import akka.persistence.{ AtLeastOnceDelivery, PersistentActor }

sealed trait Cmd
case class SayHello(deliveryId: Long, s: String) extends Cmd
case class ReceiveHello(deliveryId: Long) extends Cmd

sealed trait Evt
case class HelloSaid(s: String) extends Evt
case class HelloReceived(deliveryId: Long) extends Evt


class SendActor(destination: ActorSelection)
    extends PersistentActor with AtLeastOnceDelivery {


  override def persistenceId: String = "persistence-id"


  override def receiveCommand: Receive = {
    case s: String =>
      persist(HelloSaid(s))(updateState)
    case ReceiveHello(deliveryId) =>
      persist(HelloReceived(deliveryId))(updateState)
  }


  override def receiveRecover: Receive = {
    case evt: Evt => updateState(evt)
  }

  def updateState(evt: Evt): Unit = evt match {
    case HelloSaid(s) =>
      deliver(destination)(deliveryId => SayHello(deliveryId, s))

    case HelloReceived(deliveryId) =>
      confirmDelivery(deliveryId)
  }
}

class ReceiveActor extends Actor {
  def receive = {
    case SayHello(deliveryId, s) =>
      // ... do something with s
      sender() ! ReceiveHello(deliveryId)
  }
}
