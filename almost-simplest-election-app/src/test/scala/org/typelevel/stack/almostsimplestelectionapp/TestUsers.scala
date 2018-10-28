package org.typelevel.stack.almostsimplestelectionapp

import org.typelevel.stack.almostsimplestelectionapp.model.{Email, User, UserName}

object TestUsers {

  val users = List(
    User(UserName("gvolpe"), Email("gvolpe@github.com")),
    User(UserName("tpolecat"), Email("tpolecat@github.com")),
    User(UserName("msabin"), Email("msabin@github.com"))
  )

}
