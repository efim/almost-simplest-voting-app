package actors

import actors.RoomsManager.RequestTrackRoom
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

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

  var roomIdToActor: Map[String, ActorRef] = Map()
  var roomActorToId: Map[ActorRef, String] = Map()

  override def receive: Receive = {
    case msg@RequestTrackRoom(name) =>
      val roomActor = roomIdToActor.get(name) match {
        case Some(roomActor) => roomActor
        case None =>
          val roomActor = context.actorOf(RoomActor.props(name))
          roomIdToActor += (name -> roomActor)
          roomActorToId += (roomActor -> name)
          context.watch(roomActor)
          roomActor
      }
      roomActor.forward(msg)

    case Terminated(roomActor) =>
      val roomId = roomActorToId(roomActor)
      roomIdToActor -= roomId
      roomActorToId -= roomActor
      log.info(s"Room actor for $roomId has been terminated")

  }

}
