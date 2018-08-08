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

trait RoomsRoutes extends JsonSupport {
  implicit val system: ActorSystem

  lazy val log = Logging(system, classOf[RoomsRoutes])

//  def roomsManagerActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val roomsRoutes: Route =
    pathPrefix("rooms") {
      concat(
        pathEnd {
          get {
            complete("stub")
          }
        }
      )
    }
}
