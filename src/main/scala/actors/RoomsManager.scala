package actors

import actors.RoomsManager.RequestTrackRoom
import akka.actor.{Actor, ActorLogging, Props, Terminated}

object RoomsManager {
  def props(): Props = Props(new RoomsManager)

  final case class RequestTrackRoom(name: String)
  object ReplyTrackRoom
}

/**
  * Reply for requests to track room actors
  * store mapping of room id to room actors \ load room actors
  */
class RoomsManager extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("Rooms manager started")
  override def postStop(): Unit = log.info("Rooms manager stopped")

  override def receive: Receive = {
    case RequestTrackRoom(name) => ???
    case Terminated(roomActor) => ???
  }

}
