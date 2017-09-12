import akka.actor.{ActorRef, ActorSystem, Props}

object GuidebookMain extends App {
  val system: ActorSystem = ActorSystem("BookSystem")

  val guideProps: Props = Props[Guidebook]
  val guidebook: ActorRef =
    system.actorOf(guideProps, "guidebook")
}
