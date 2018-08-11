package domain

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.TryValues._
import Room._

class TestRoom extends FlatSpec with Matchers {

  trait Users {
    val users = Set(RoomUser("u1"), RoomUser("u2"))
    import scala.collection.breakOut
    val volunteeringHistory: Map[RoomUser, Int] = users.map(user => (user, 1))(breakOut)
  }

  trait Room extends Users{
    val room = Room("testRoom", users, volunteeringHistory = volunteeringHistory)
  }

  trait RoomWithPoll extends Users {
    val room = Room(
      "testRoom",
      users = users,
      volunteeringHistory = volunteeringHistory,
      polls = Seq(Poll(history = volunteeringHistory)))
  }

  "A room" should "add new user" in new Room  {
    val newUser1 = RoomUser("testUser")
    assert(!room.users(newUser1))

    val maybeRoom = room.addUser(newUser1)
    maybeRoom.success.value.users shouldBe (room.users + newUser1)
  }

  it should "allow to start new poll" in new Room {
    val startedPoll = room.startNewPoll().success.value.getActivePoll

    startedPoll.success.value should be ('isOpen)
    startedPoll.success.value.volunteers should be ('isEmpty)
    startedPoll.success.value.history shouldBe room.volunteeringHistory
  }
  
  "A room with users" should "not allow to add duplicate username" in new Room {
    val newUser1 = RoomUser("u1")
    assert(room.users(newUser1))

    val maybeRoom = room.addUser(newUser1)
    maybeRoom.failure.exception should have message "Cannot join the room. Duplicate username: u1"
  }

  it should "allow removal of single user" in new Room {
    val userToRemove = room.users.head

    val updatedRoom = room.removeUser(userToRemove)

    updatedRoom.users shouldBe (room.users - userToRemove)
  }

  "A room with users and active poll" should "allow to add user to poll" is (pending)

  it  should "allow to remove user from poll" is (pending)

  it should "not allow to add to poll a user that is not in the room" is (pending)

  it should "remove user from poll on user leaving the room" is (pending)

  it should "allow to finalize active poll resulting in correct update of volunteering history" is (pending)
}
