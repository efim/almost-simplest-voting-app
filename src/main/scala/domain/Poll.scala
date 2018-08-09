package domain

object Poll {

  sealed trait PollStatus

  object Open extends PollStatus

  object Closed extends PollStatus

}

case class Poll(
                 status: Poll.PollStatus,
                 volunteers: Set[RoomUser],
                 history: Room.VolunteeringHistory
               ) extends PollOps

trait PollOps {
  self: Poll =>

  import Poll._
  def isOpen: Boolean = self.status == Open
  def isClosed: Boolean = !self.isOpen

  def addVolunteer(user: RoomUser): Poll = self.copy(volunteers = volunteers + user)
  def removeVolunteer(user: RoomUser): Poll = self.copy(volunteers = volunteers - user)

}
