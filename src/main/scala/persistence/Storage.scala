package persistence

import persistence.Storage.InsertionError

import scala.concurrent.Future

/*
example of doobie persistence: https://github.com/lloydmeta/http4s-doobie-docker-scratchpad
 */

object Storage {
  final case class InsertionError(underlying: Throwable) extends Error
}

trait Storage[K, T] {
  import Storage._

  def get(id: K): Future[Option[T]]
  def list(): Future[Seq[T]]
  def insert(item: T): Future[Either[InsertionError, K]]
  def delete(id: K): Future[Int]
  def update(id: K, poll: T): Future[Int]
}

trait MemoryStorage[K, T] extends Storage[K, T] {

  var idToItem: Map[K, T] = Map()
  protected def getKey(item: T):  K

  override def get(id: K): Future[Option[T]] = Future.successful(idToItem.get(id))

  override def list(): Future[Seq[T]] = Future.successful(idToItem.values.toSeq)

  override def insert(item: T): Future[Either[InsertionError, K]] = {
    val id = getKey(item)
    idToItem = idToItem + (id -> item)
    Future.successful(Right(id))
  }

  override def delete(id: K): Future[Int] = {
    val deleted = idToItem.get(id) match {
      case Some(_) =>
        idToItem -= id
        1
      case None => 0
    }
    Future.successful(deleted)
  }

  override def update(id: K, item: T): Future[Int] = {
    val updated = idToItem.get(id) match {
      case Some(_) =>
        idToItem += (id -> item)
        1
      case None => 0
    }
    Future.successful(updated)
  }


}