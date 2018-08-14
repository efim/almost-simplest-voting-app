package persistence

import data.domain.Poll
/*
example of doobie persistence: https://github.com/lloydmeta/http4s-doobie-docker-scratchpad
 */

object PollStorage {
  def memory[K]()(implicit ms: MemoryStorage[K, Poll]): Storage[K, Poll] = ms

  implicit object MemoryLongPollStorage extends MemoryStorage[Long, Poll] {
    var key: Long = 0

    override def getKey(item: Poll): Long = {
      key += 1
      key
    }
  }
}
