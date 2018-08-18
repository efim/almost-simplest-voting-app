package persistence

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}
import PollStorage._
import data.domain.{Poll, RoomUser}

import scala.concurrent.Await
import scala.concurrent.duration._

class TestMemoryStorage extends FlatSpec with Matchers with EitherValues with OptionValues {

  "Poll memory storage singleton test" should "be big and stupid" in {
    val atMost = 2 seconds

    val pollDao = PollStorage.memory()
    val poll1 = Poll()
    val poll2 = Poll(history = Map(RoomUser("u1") -> 1, RoomUser("u2") -> 2))
    val key1 = Await.result(pollDao.insert(poll1), atMost).right.get
    val key2 = Await.result(pollDao.insert(poll2), atMost).right.get

    Await.result(pollDao.get(key1), atMost).value should be (poll1)
    Await.result(pollDao.get(key2), atMost).value should be (poll2)

    Await.result(pollDao.list(), atMost) contains poll1
    Await.result(pollDao.list(), atMost) contains poll2

    val poll3 = poll1.copy(volunteers = Set(RoomUser("u3")))
    pollDao.update(key1, poll3)
    Await.result(pollDao.get(key1), atMost).value should be (poll3)
  }

}
