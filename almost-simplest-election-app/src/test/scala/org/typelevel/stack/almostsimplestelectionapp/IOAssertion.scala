package org.typelevel.stack.almostsimplestelectionapp

import cats.effect.IO

object IOAssertion {
  def apply[A](ioa: IO[A]): A = ioa.unsafeRunSync()
}
