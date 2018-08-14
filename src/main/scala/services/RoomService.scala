package services

import persistence.{PollStorage, RoomStorage, Storage}
import PollStorage._
import RoomStorage._
import data.domain._

import scala.concurrent.Future

class RoomService {

  val roomDao: Storage[RoomId, Room] = RoomStorage.memory()

  def create(roomId: RoomId): Future[ActionPerformed] = ???

  def delete(roomId: RoomId): Future[ActionPerformed] = ???

  def addUser(roomId: RoomId, user: RoomUser): Future[ActionPerformed] = {
    // fetch room from
    ???
  }

  def removeUser(roomId: RoomId, user: RoomUser): Future[ActionPerformed] = ???

  def get(roomId: RoomId): Future[Room] = ???

  def startPoll(roomId: RoomId): Future[ActionPerformed] = ???

  def finalizePoll(roomId: RoomId): Future[ActionPerformed] = ???

  def addUserToPoll(roomid: RoomId, user: RoomUser): Future[ActionPerformed] = ???

  def removeUserFromPoll(roomid: RoomId, user: RoomUser): Future[ActionPerformed] = ???
}
