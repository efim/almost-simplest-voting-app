package persistence

import actors.PollActor.Poll

/*
example of doobie persistence: https://github.com/lloydmeta/http4s-doobie-docker-scratchpad
 */

object PollStorage {
  def memoryStorage[K](implicit ms: MemoryStorage[K, Poll]): Storage[K, Poll] = ms
}

trait PollStorage {

  implicit object MemoryLongPollStorage extends LongKeyStorage[Poll] with MemoryStorage[Long, Poll]

}