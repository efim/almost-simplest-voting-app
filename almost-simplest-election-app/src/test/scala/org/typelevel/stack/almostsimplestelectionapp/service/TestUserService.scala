package org.typelevel.stack.almostsimplestelectionapp.service

import cats.effect.IO
import org.typelevel.stack.almostsimplestelectionapp.TestUsers.users
import org.typelevel.stack.almostsimplestelectionapp.model.UserName
import org.typelevel.stack.almostsimplestelectionapp.repository.algebra.UserRepository

object TestUserService {

  private val testUserRepo: UserRepository[IO] =
    (username: UserName) => IO {
      users.find(_.username.value == username.value)
    }

  val service: UserService[IO] = new UserService[IO](testUserRepo)

}
