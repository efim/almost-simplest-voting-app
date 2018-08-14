package data

object domain {
  final case class Room(name: String,
                         users: Set[RoomUser] = Set(),
                         speaker: Option[RoomUser] = None,
                         volunteeringHistory: Map[RoomUser, Int] = Map(),
                         activePollId: Option[PollId] = None)

  final case class RoomUser(name: String)

  final case class Poll(winner: Option[RoomUser] = None,
                  volunteers: Set[RoomUser] = Set(),
                  history: Map[RoomUser, Int] = Map())

  final case class ActionPerformed(description: String)

  type RoomId = String
  type PollId = Long
}
