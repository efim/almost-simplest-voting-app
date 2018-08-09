package domain

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.TryValues._
import Room._

class TestRoom extends FlatSpec with Matchers {

  "A room" should "add new user" in {
    val room = Room("testRoom", Set(RoomUser("u1"), RoomUser("u2")))
    val newUser1 = RoomUser("testUser")

    val maybeRoom = room.addUser(newUser1)
    maybeRoom.success.value.users shouldBe (room.users + newUser1)
  }

  "A room with users" should "not allow to add duplicate username" in {
    val room = Room("testRoom", Set(RoomUser("u1"), RoomUser("u2")))
    val newUser1 = RoomUser("u1")

    val maybeRoom = room.addUser(newUser1)
    maybeRoom.failure.exception should have message "Cannot join the room. Duplicate username: u1"
  }

  "A room with users" should "allow removal of single user" in {
    val userToRemove = RoomUser("u2")
    val room = Room("testRoom", Set(RoomUser("u1"), userToRemove))

    val updatedRoom = room.removeUser(userToRemove)

    updatedRoom.users shouldBe (room.users - userToRemove)
  }
}
