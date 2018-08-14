package persistence

import data.domain.Room

object RoomStorage {
  def memory[K]()(implicit ms: MemoryStorage[K, Room]): Storage[K, Room] = ms

  implicit object RoomNameStorage extends MemoryStorage[String, Room] {
    override def getKey(item: Room): String = item.name
  }

}
