package persistence

import org.scalatest.{FlatSpec, Matchers, EitherValues, OptionValues}
import PollStorage._
import actors.PollActor.Poll
import actors.RoomActor.RoomUser

class TestMemoryStorage extends FlatSpec with Matchers with EitherValues with OptionValues {

  "Poll memory storage singleton test" should "be big and stupid" in {
    val pollDao = PollStorage.memoryStorage()
    val poll1 = Poll()
    val poll2 = Poll(history = Map(RoomUser("u1") -> 1, RoomUser("u2") -> 2))
    val key1 = pollDao.insert(poll1).right.get
    val key2 = pollDao.insert(poll2).right.get

    pollDao.get(key1).value should be (poll1)
    pollDao.get(key2).value should be (poll2)

    pollDao.list() contains poll1
    pollDao.list() contains poll2

    val poll3 = poll1.copy(volunteers = Set(RoomUser("u3")))
    pollDao.update(key1, poll3)
    pollDao.get(key1).value should be (poll3)
  }

}
