package ElevatorControlSystem

case class ElevatorState(elevatorId: Int, floorNumber: Int, floors: Int, goalFloorNumbers: Set[Int]) {
  require(floorNumber >= 0 && floorNumber <= floors, s"floorNumber: $floorNumber argument must be between 0 - $floors (building number of floors)")

  goalFloorNumbers.foreach(goalFloor => require(goalFloor >= 0 && goalFloor <= floors, s"goalFloor: $goalFloor argument must be between 0 - $floors (building number of floors)"))

  def update(newFloorNumber: Int, goalFloor: Int): ElevatorState = {
    require(newFloorNumber >= 0 && newFloorNumber <= floors, s"newFloorNumber: $newFloorNumber argument must be between 0 - $floors (building number of floors)")
    require(goalFloor >= 0 && goalFloor <= floors, s"goalFloor: $goalFloor argument must be between 0 - $floors (building number of floors)")

    val filteredGoalFloorNumbers = goalFloorNumbers.filterNot(floorNumber => floorNumber == newFloorNumber)
    val updatedGoalFloorNumbers = filteredGoalFloorNumbers.+(goalFloor)

    ElevatorState(elevatorId, newFloorNumber, floors, updatedGoalFloorNumbers)
  }

  def state(): ElevatorState = this

  def nextFloor(goalFloor: Int): Int = {
    goalFloor - floorNumber match {
      case diff if diff < 0 =>
        println("Elevator going down")
        floorNumber - 1
      case diff if diff > 0 =>
        println("Elevator going up")
        floorNumber + 1
      case _ =>
        println("Elevator already at destination, no move needed.")
        floorNumber
    }
  }
}