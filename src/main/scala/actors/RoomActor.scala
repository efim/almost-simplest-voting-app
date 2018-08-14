package actors

import actors.PollActor.Poll

import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.util.Timeout
import persistence.{PollStorage, Storage}
import persistence.PollStorage._

import scala.concurrent.Future


/**
  * Contains logic for interacting with the room:
  * - getting current state
  * current speaker, users, current poll, history of volunteering
  * - adding \ removing users
  * - starting new poll \ incorporating poll results
  */
object RoomActor {
  def props(name: String): Props = Props(new RoomActor(name))

  final case class ActionPerformed(description: String)

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
  var activePollActor: Option[ActorRef] = None
  val pollDao: Storage[Long, Poll] = PollStorage.memoryStorage()

  implicit val timeout = Timeout(5.seconds)

  override def preStart(): Unit = log.info(s"Room actor for $name started")
  override def postStop(): Unit = log.info(s"Room actor for $name stopped")

  override def receive: Receive = {
    case RoomsManager.RequestTrackRoom(`name`) =>
      sender() ! RoomsManager.ReplyTrackRoom
    case RoomsManager.RequestTrackRoom(roomName) =>
      log.warning(s"Received RequestTrackRoom for $roomName; ignoring")

    case AddUser(user) =>
      val updatedUsers = room.users + user
      room = room.copy(users = updatedUsers)
      sender() ! ActionPerformed(s"Added user ${user.name} to the room")

    case RemoveUser(user) => roomUserAction(user, { user =>
      val updatedUsers = room.users
      room = room.copy(users = updatedUsers)
      sender() ! ActionPerformed(s"User ${user.name} is removed from the room")
    })

    case AskRoomInfo => sender() ! room

    case StartPoll =>
      val actorRef = activePollActor match {
        case Some(ref) => ref
          // turd: this is somewhat for the pollsManager
        case None => {
          val poll = room.activePollId match {
            case Some(pollId) => pollDao.get(pollId).get //this is already insane amount of stateful interaction
            case None =>
              // create new one also from repository?
              val newPoll = Poll(history = room.volunteeringHistory)
              // turd: this will become db interaction, so all of this should become IO somehow
              pollDao.insert(newPoll)
          }
          // if room has id for active poll - get case class from repository
          // create new actor
          // watch this actor
        }
      }


    case Terminated(pollActor) => activePollActor match {
      case Some(`pollActor`) => activePollActor = None
      case _ => log.warning(s"Received termination notification from not tracked PollActor child; ignoring")
    }

    case msg@FinalizePoll => activePollAction({ pollActor: ActorRef =>
      import akka.pattern._
      val futureWinner: Future[RoomUser] = pollActor.ask(msg).mapTo[RoomUser]
      // pipe to self
      // futureWinner.onComplete()


    })
      // if there is a poll
      // ask about winner
      // update history - clear for winner; tick up for everyone else
      // kill the poll; set active poll id of room to none
      ???

    case msg@AddUserToActivePoll(user) => roomUserAction(user, { user =>
      activePollAction(_.forward(msg))
    })

    case msg@RemoveUserFromActivePoll(user) => roomUserAction(user, { user =>
      activePollAction(_.forward(msg))
    })
  }

  def roomUserAction(user: RoomUser, action: RoomUser => Unit): Unit = if (room.users(user)) {
    action(user)
  } else {
    sender() ! ActionPerformed(s"User ${user.name} was not present in the room")
  }

  def activePollAction(action: ActorRef => Unit): Unit = activePollActor match {
    case Some(pollActor) => action(pollActor)
    case None => sender() ! ActionPerformed(s"Room does not have an active poll")
  }


}
