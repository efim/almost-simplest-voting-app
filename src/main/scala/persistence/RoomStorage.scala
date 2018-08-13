package persistence

import actors.RoomActor.Room

object RoomStorage {
  def memory[K](implicit ms: MemoryStorage[K, Room]): Storage[K, Room] = ms
}

trait RoomStorage {

  implicit object RoomLongStorage extends LongKeyStorage[Room] with MemoryStorage[Long, Room]

}
