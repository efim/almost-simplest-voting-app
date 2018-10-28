package org.typelevel.stack.almostsimplestelectionapp

import org.typelevel.stack.almostsimplestelectionapp.model._

package object repository {

  type UserDTO = (Int, String, String)

  implicit class UserConversions(dto: UserDTO) {
    def toUser: User = User(
      username = UserName(dto._2),
      email = Email(dto._3)
    )
  }

}
