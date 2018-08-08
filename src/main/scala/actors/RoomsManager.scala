package actors

import akka.actor.{Actor, ActorLogging, Props}

object RoomsManager {
  def props(): Props = Props(new RoomsManager)
}

class RoomsManager extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("Rooms manager started")
  override def postStop(): Unit = log.info("Rooms manager stopped")

  override def receive: Receive = Actor.emptyBehavior

}
