package routes

import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.util.Timeout
import data.domain.{ActionPerformed, NewRoom, Room, RoomId}
import services.RoomService
import spray.json.JsValue

import scala.concurrent.Future

trait RoomsRoutes extends JsonSupport {
  implicit val system: ActorSystem

  lazy val log = Logging(system, classOf[RoomsRoutes])

  //  def roomsManagerActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val roomsRoutes: Route =
    pathPrefix("rooms") {
      concat(
        pathEnd {
          concat(
            get {
              complete("stub")
            },
            post {
              entity(as[NewRoom]) { newRoom =>
                val roomCreated: Future[Either[Error, ActionPerformed]] = RoomService.create(newRoom.name)
                onSuccess(roomCreated) {
                  case Right(actionPerformed) =>
                    log.info(s"Created room: $actionPerformed")
                    complete((StatusCodes.Created, actionPerformed))
                  case Left(error) => failWith(error)
                }
              }
            }
          )
        },
        path(Segment) { name =>
          concat(
            get {
              val maybeRoom: Future[Option[Room]] = RoomService.get(name)
              rejectEmptyResponse {
                complete(maybeRoom)
              }
            }
          )
        }
      )
    }

}
