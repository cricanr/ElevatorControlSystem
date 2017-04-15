package ElevatorControlSystem

sealed trait Direction{def direction: Int}

case object Up extends Direction {
  val direction = 1
}

case object Down extends Direction {
  val direction = 1
}

object Direction {
  def apply(direction: Int): Direction = {
    direction match {
      case dir if dir >= 0 => Up
      case _ => Down
    }
  }
}
