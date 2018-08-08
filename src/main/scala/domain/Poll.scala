package domain

trait PollStatus
case class Open() extends PollStatus
case class Closed() extends PollStatus

case class Poll(id: Long, status: PollStatus, volunteers: Set[RoomUser], history: Room.VolunteeringHistory)
