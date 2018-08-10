package domain

object Poll {

  sealed trait PollStatus
  object Open extends PollStatus
  object Closed extends PollStatus

}

case class Poll(
                 status: Poll.PollStatus = Poll.Open,
                 winner: Option[RoomUser] = None,
                 volunteers: Set[RoomUser] = Set(),
                 history: Map[RoomUser, Int] = Map()
               ) extends PollOps

trait PollOps {
  self: Poll =>

  import Poll._
  def isOpen: Boolean = self.status == Open
  def isClosed: Boolean = !self.isOpen

  def addVolunteer(user: RoomUser): Poll = self.copy(volunteers = volunteers + user)
  def removeVolunteer(user: RoomUser): Poll = self.copy(volunteers = volunteers - user)

  def finalizePoll(): Poll = {
    val winner = self.calculateWinner
    assert(winner match {
      case None => self.volunteers.isEmpty
      case Some(user) => self.volunteers.contains(user)
    })

    self.copy(
      winner = winner,
      status = Poll.Closed
    )
  }

  private def calculateWinner: Option[RoomUser] = {
    import scala.collection.breakOut

    // take only volunteers with weights from all prevoious history
    val weightedUsers: Seq[(RoomUser, Int)] = volunteers.map(user => user -> (history.getOrElse(user, 0) + 1))(breakOut)

    val rand = scala.util.Random
    val selection: Int = rand(weightedUsers.map(_._2).sum)

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

}
