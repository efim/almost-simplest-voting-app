package org.typelevel.stack.almostsimplestelectionapp.repository

import org.typelevel.stack.almostsimplestelectionapp.model.{User, UserName}

object algebra {

  trait UserRepository[F[_]] {
    def findUser(username: UserName): F[Option[User]]
  }

}
