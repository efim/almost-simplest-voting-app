package actors

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Contains logic for interacting with the room:
  * - getting current state
  *   current speaker, users, current poll, history of volunteering
  * - adding \ removing users
  * - starting new poll \ incorporating poll results
  */
object RoomActor {
  def props(name: String): Props = Props(new RoomActor(name))

  final case class ActrionPerformed(description: String)
  final case class AddUser(user: RoomUser)
  final case class RemoveUser(user: RoomUser)

  final object AskRoomInfo

  final object StartPoll // will return poll id which is "form db"
  final object FinalizePoll

  final case class AddUserToActivePoll(user: RoomUser)
  final case class RemoveUserFromActivePoll(user: RoomUser)

  final case class Room(
                   name: String,
                   users: Set[RoomUser] = Set(),
                   speaker: Option[RoomUser] = None,
                   volunteeringHistory: Map[RoomUser, Int] = Map(),
                   activePollId: Option[Long] = None,
                 )
  final case class RoomUser(name: String)
}

class RoomActor(name: String) extends Actor with ActorLogging {
  import actors.RoomActor._
  var room: Room = Room(name)


  override def preStart(): Unit = log.info(s"Room actor for $name started")
  override def postStop(): Unit = log.info(s"Room actor for $name stopped")

  override def receive: Receive = {
    case AddUser(user) => ???
    case RemoveUser(user) => ???
    case AskRoomInfo => sender() ! room
    case StartPoll => ???
    case FinalizePoll => ???
    case AddUserToActivePoll(user) => ???
    case RemoveUserFromActivePoll(user) => ???
  }
}
