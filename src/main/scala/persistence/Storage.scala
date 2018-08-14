package persistence

import persistence.Storage.InsertionError

/*
example of doobie persistence: https://github.com/lloydmeta/http4s-doobie-docker-scratchpad
 */

object Storage {
  sealed trait Error
  final case class InsertionError(underlying: Throwable) extends Error
}

trait Storage[K, T] {
  import Storage._

  def get(id: K): Option[T]
  def list(): Seq[T]
  def insert(item: T): Either[InsertionError, K]
  def delete(id: K): Int
  def update(id: K, poll: T): Int
}

trait MemoryStorage[K, T] extends Storage[K, T] {

  var idToItem: Map[K, T] = Map()
  def getKey(item: T):  K

  override def get(id: K): Option[T] = idToItem.get(id)

  override def list(): Seq[T] = idToItem.values.toSeq

  override def insert(item: T): Either[InsertionError, K] = {
    val id = getKey(item)
    idToItem = idToItem + (id -> item)
    Right(id)
  }

  override def delete(id: K): Int = idToItem.get(id) match {
    case Some(_) =>
      idToItem -= id
      1
    case None => 0
  }

  override def update(id: K, item: T): Int = idToItem.get(id) match {
    case Some(_) =>
      idToItem += (id -> item)
      1
    case None => 0
  }


}