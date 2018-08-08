package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object AppSupervisor {
  def props(): Props = Props(new AppSupervisor)
}

class AppSupervisor extends Actor with ActorLogging {
  val roomsManager: ActorRef = context.actorOf(RoomsManager.props(), "rooms-manager")

  override def preStart(): Unit =
    log.info("Simple voting application started")

  override def postStop(): Unit = log.info("Simple voting application stopped")

  override def receive: Receive = Actor.emptyBehavior
}
