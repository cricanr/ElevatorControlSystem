package ElevatorControlSystem

case class ElevatorState(elevatorId: Int, floorNumber: Int, floors: Int, goalFloorNumbers: Set[Int]) {
  def update(currentFloor: Int, goalFloor: Int): ElevatorState = {
    val filteredGoalFloorNumbers = goalFloorNumbers.filterNot(floorNumber => floorNumber == currentFloor)
    val updatedGoalFloorNumbers = filteredGoalFloorNumbers.+(goalFloor)

    ElevatorState(elevatorId, currentFloor, floors, updatedGoalFloorNumbers)
  }

  def state(): ElevatorState = this

  def nextFloor(goalFloor: Int): Int = {
    // taking the first goal if exists
    goalFloor - floorNumber match {
      case d if d < 0 =>
        println("Elevator going down")
        floorNumber - 1
      case d if d > 0 =>
        println("Elevator going up")
        floorNumber + 1
      case _ =>
        println("Elevator already at destination, no move needed.")
        floorNumber
    }
  }
}