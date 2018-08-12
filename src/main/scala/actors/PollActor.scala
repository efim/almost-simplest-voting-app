package actors

import actors.PollActor.Poll
import actors.RoomActor.RoomUser
import akka.actor.{Actor, ActorLogging, Props}


/**
  * Logic for open room poll:
  * - add \ remove volunteers
  * - calculate random volunteer taking into account previous volunteering history
  */

object PollActor {
  def props(poll: Poll): Props = Props(new PollActor(poll))

  final case class PollResults(winner: RoomUser)

  case class Poll(winner: Option[RoomUser] = None,
                   volunteers: Set[RoomUser] = Set(),
                   history: Map[RoomUser, Int] = Map())

}

class PollActor(poll: Poll) extends Actor with ActorLogging {
  var _poll: Poll = poll

  override def preStart(): Unit = log.info("Poll actor started")
  override def postStop(): Unit = log.info("Poll actor stopped")

  override def receive: Receive = {
    case RoomActor.FinalizePoll => ???
    case RoomActor.AddUserToActivePoll(user) => ???
    case RoomActor.RemoveUserFromActivePoll(user) => ???
  }

/*
  private def calculateWinner: Option[RoomUser] = {
    import scala.collection.breakOut

    // take only volunteers with weights from all prevoious history
    val weightedUsers: Seq[(RoomUser, Int)] = volunteers.map(user => user -> (history.getOrElse(user, 0) + 1))(breakOut)

    val rand = scala.util.Random
    val selection: Int = rand.nextInt(weightedUsers.map(_._2).sum)

    def findSelected(acc: Int, restOfWeightedUsers: Seq[(RoomUser, Int)]): RoomUser = restOfWeightedUsers match {
      case Seq(last) => last._1
      case weightedUser::rest => if (selection < acc) {
        weightedUser._1
      } else {
        findSelected(acc + rest.head._2, rest)
      }
    }

    weightedUsers match {
      case Nil => None
      case _ => Some(findSelected(weightedUsers.head._2, weightedUsers))
    }
  }
*/
}
