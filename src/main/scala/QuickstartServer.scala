import actors.AppSupervisor
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import routes.RoomsRoutes

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object QuickstartServer extends App with RoomsRoutes {

  implicit val system: ActorSystem = ActorSystem("ASVotingHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val applicationSupervisor: ActorRef = system.actorOf(AppSupervisor.props(), "app-supervisor")
//  val roomsManagerActor: ActorRef = ???

  lazy val routes: Route = roomsRoutes

  Http().bindAndHandle(routes, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)
}
