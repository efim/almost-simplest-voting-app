package domain

import scala.util.{Failure, Success, Try}

object Room {
  type VolunteeringHistory = Map[RoomUser, Int]
  def VolunteeringHistory(kv: (RoomUser, Int)*) = Map(kv: _*)
}

case class Room(id: String, users: Set[RoomUser], history: Room.VolunteeringHistory) extends RoomOps

final case class RoomUser(name: String)

final case class NonUniqueUserException(user: RoomUser)
  extends Exception()

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


}