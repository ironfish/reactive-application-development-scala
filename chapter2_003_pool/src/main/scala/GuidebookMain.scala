import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig

object GuidebookMain extends App {
  val system: ActorSystem = ActorSystem("BookSystem")

  val guideProps: Props = Props[Guidebook]
  val routerProps: Props =
    FromConfig.props(guideProps)
  val guidebook: ActorRef =
    system.actorOf(routerProps, "guidebook")
}
