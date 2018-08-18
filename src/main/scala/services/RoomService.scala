package services

import persistence.{PollStorage, RoomStorage, Storage}
import PollStorage._
import RoomStorage._
import data.domain._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RoomService {

  val roomDao: Storage[RoomId, Room] = RoomStorage.memory()

  def create(roomId: RoomId): Future[Either[Error, ActionPerformed]] = {
    val room = Room(name=roomId)
    roomDao.insert(room).map (_.map(id => ActionPerformed(s"Room with id $id is created")))

  }

  def delete(roomId: RoomId): Future[ActionPerformed] = ???

  def addUser(roomId: RoomId, user: RoomUser): Future[ActionPerformed] = {
    // fetch room from
    ???
  }

  def removeUser(roomId: RoomId, user: RoomUser): Future[ActionPerformed] = ???

  def get(roomId: RoomId): Future[Option[Room]] = roomDao.get(roomId)

  def startPoll(roomId: RoomId): Future[ActionPerformed] = ???

  def finalizePoll(roomId: RoomId): Future[ActionPerformed] = ???

  def addUserToPoll(roomid: RoomId, user: RoomUser): Future[ActionPerformed] = ???

  def removeUserFromPoll(roomid: RoomId, user: RoomUser): Future[ActionPerformed] = ???
}
