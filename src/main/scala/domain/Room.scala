package domain

import scala.util.{Failure, Success, Try}

object Room {
  type VolunteeringHistory = Map[RoomUser, Int]

  def VolunteeringHistory(kv: (RoomUser, Int)*) = Map(kv: _*)

  def apply(name: String, users: Set[RoomUser]): Room = new Room(id, users)
}

case class Room(
                 id: String,
                 users: Set[RoomUser],
                 history: Room.VolunteeringHistory,
                 polls: Seq[Poll]
               ) extends RoomOps {
  def this(id: String, users: Set[RoomUser]) {
    this(id, users, Room.VolunteeringHistory(), Seq())
  }
}

final case class RoomUser(name: String)

trait RoomOps {
  self: Room =>

  def addUser(user: RoomUser): Try[Room] = {
    if (self.users(user)) {
      Failure(new IllegalArgumentException(s"Cannot join the room. Duplicate username: ${user.name}"))
    } else {
      Success(self.copy(
        users = self.users + user
      ))
    }
  }

  def removeUser(user: RoomUser): Room =
    self.copy(users = users - user)
    //TODO : if there is an active poll and user is volunteering - remove them

  def startNewPoll(): Try[Room] =
    self.polls match {
      case Seq(latest, _) if latest.isOpen =>
        Failure(new IllegalArgumentException(s"Cannot start new poll: only one active poll is allowed"))
      case currentPolls =>
        val poll = Poll(Poll.Open, Set(), self.history)
        Success(self.copy(polls = poll +: currentPolls))
    }

  def activePoll: Option[Poll] = self.polls match {
    case Seq(latest, _) if latest.isOpen => Some(latest)
    case _ => None
  }

  def updateActivePoll(f: Poll => Poll): Try[Room] = self.activePoll match {
    case Some(poll) =>
      Success(self.copy(polls = f(poll) +: polls.tail))
    case None => Failure(new IllegalStateException(s"Room has no active poll to update"))
  }

  def withRequiredUser(user: RoomUser): Try[Room] = if (self.users(user)) {
    Success(self)
  } else {
    Failure(new IllegalArgumentException(s"Cannot perform operation on user: $user because user is not participant of the room"))
  }

  def addUserToActivePoll(user: RoomUser): Try[Room] =
    self.withRequiredUser(user).flatMap(_.updateActivePoll(_.addVolunteer(user)))

  def removeUserFromActivePoll(user: RoomUser): Try[Room] =
    self.withRequiredUser(user).flatMap(_.updateActivePoll(_.removeVolunteer(user)))

  def finalizeActivePoll(): Room = {
    // get updated poll - delegate poll finalization: selection of winner, status change
    // update volunteering history to add unsuccessful attempts to non-winners, to zero out them for the winner
  }

}