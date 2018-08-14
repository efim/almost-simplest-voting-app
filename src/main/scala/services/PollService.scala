package services

import data.domain._
import persistence.{PollStorage, Storage}
import PollStorage._

import scala.concurrent.Future

class PollService {
  val pollDao: Storage[PollId, Poll] = PollStorage.memory()

  def startNew(roomId: RoomId, history: Map[RoomUser, Int]): Future[PollId] = ???

  def addVolunteer(pollId: PollId, user: RoomUser): Future[ActionPerformed] = ???

  def removeVolunteer(pollId: PollId, user: RoomUser): Future[ActionPerformed] = ???

  def finalizePoll(pollId: PollId): Future[RoomUser] = ???

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
