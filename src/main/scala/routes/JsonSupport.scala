package routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import data.domain.{ActionPerformed, NewRoom, Room, RoomUser}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat1(RoomUser)
  implicit val newRoomJsonFormat = jsonFormat1(NewRoom)
  implicit val roomJsonFormat = jsonFormat5(Room)
  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)

}
