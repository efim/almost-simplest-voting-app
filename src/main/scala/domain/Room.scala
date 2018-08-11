package domain

import scala.util.{Failure, Success, Try}

case class Room(
                 id: String,
                 users: Set[RoomUser] = Set(),
                 volunteeringHistory: Map[RoomUser, Int] = Map(),
                 polls: Seq[Poll] = Seq()
               ) extends RoomOps

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

  def removeUser(user: RoomUser): Room = {
    val roomWithoutUser = self.copy(users = users - user)

    roomWithoutUser.removeUserFromActivePoll(user).getOrElse(roomWithoutUser)
  }

  def startNewPoll(): Try[Room] =
    self.polls match {
      case Seq(latest, _) if latest.isOpen =>
        Failure(new IllegalArgumentException(s"Cannot start new poll: only one active poll is allowed"))
      case currentPolls =>
        val poll = Poll(history = self.volunteeringHistory)
        Success(self.copy(polls = poll +: currentPolls))
    }

  def getActivePoll: Try[Poll] = self.polls match {
    case latest::_ if latest.isOpen => Success(latest)
    case _ => Failure(new IllegalStateException("Room doesn't have an active poll"))
  }

  def updateActivePoll(f: Poll => Poll): Try[Room] =
    self.getActivePoll.map(poll => self.copy(polls = f(poll) +: polls.tail))

  def withRequiredUser(user: RoomUser): Try[Room] = if (self.users(user)) {
    Success(self)
  } else {
    Failure(new IllegalArgumentException(s"Cannot perform operation on user: $user because user is not participant of the room"))
  }

  def addUserToActivePoll(user: RoomUser): Try[Room] =
    for {
      checkedRoom <- self.withRequiredUser(user)
      withUpdatedPoll <- checkedRoom.updateActivePoll(_.addVolunteer(user))
    } yield withUpdatedPoll

  def removeUserFromActivePoll(user: RoomUser): Try[Room] =
    for {
      checkedRoom <-self.withRequiredUser(user)
      withUpdatedPoll <- checkedRoom.updateActivePoll(_.removeVolunteer(user))
    } yield withUpdatedPoll

  def finalizeActivePoll(): Try[Room] = {

    def updateHistoryWithPollResults(room: Room, poll: Poll): Room = {
      require(poll.isClosed)

      val updatedParticipantHistory = poll.volunteers.map(user => user -> (volunteeringHistory.getOrElse(user, 0) + 1))
      val updatedHistoryForAll = room.volunteeringHistory ++ updatedParticipantHistory
      val finalHistory = poll.winner match {
        case None => updatedHistoryForAll
        case Some(user) => updatedHistoryForAll - user
      }
      room.copy(
        volunteeringHistory = finalHistory
      )
    }

    // get updated poll - delegate poll finalization: selection of winner, status change
    // update volunteering history to add unsuccessful attempts to non-winners, to zero out them for the winner
    for {
      finalizedPoll <- self.getActivePoll.map(_.finalizePoll())
      withUpdatedPoll <- self.updateActivePoll(_ => finalizedPoll)
    } yield updateHistoryWithPollResults(withUpdatedPoll, finalizedPoll)
  }

}