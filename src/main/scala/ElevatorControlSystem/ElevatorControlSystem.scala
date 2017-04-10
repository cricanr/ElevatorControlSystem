package ElevatorControlSystem

case class ElevatorState(elevatorId: Int, floorNumber: Int, floors: Int, goalFloorNumbers: Set[Int]) {
  def update(currentFloor: Int, goalFloor: Int): Unit = {
        val filteredGoalFloorNumbers = goalFloorNumbers.filterNot(floorNumber => floorNumber == currentFloor)
        val updatedGoalFloorNumbers = filteredGoalFloorNumbers.+(goalFloor)

        ElevatorState(elevatorId, floorNumber, floors, updatedGoalFloorNumbers)
      }

  def state(): ElevatorState = this

  def nextFloor(): Int = {
    // taking the first goal if exists
    goalFloorNumbers.headOption.map { floor =>
      floor - floorNumber match {
        case d if d < 0 =>
          println("Elevator going down")
          floorNumber - 1
        case d if d > 0 =>
          println("Elevator going up")
          floorNumber + 1
        case d if d == 0 =>
          println("Elevator already at destination, no move needed.")
          floorNumber
      }
    }.getOrElse(floorNumber)
  }
}

case class PickupRequest(pickupFloor: Int, direction: Int)

trait ElevatorControlSystem {
  def status(): Seq[ElevatorState]

  def update(elevatorStatus: ElevatorState)

  def pickup(pickupRequest: PickupRequest)

  def step()
}

class ElevatorControlSystemImpl(numberOfElevators: Int, numberOfFloors: Int) extends ElevatorControlSystem {
  val elevators = for (i <- 0 until numberOfElevators) yield ElevatorState(i, 0, numberOfFloors, Set.empty)
  val pickupRequests = Seq[PickupRequest]()

  override def status(): Seq[ElevatorState] = {
    elevators
  }

  override def update(elevatorState: ElevatorState): Unit = {
    val maybeElevatorState = elevators.find(elevator => elevator.elevatorId == elevatorState.elevatorId)
    val maybeIndex = maybeElevatorState.map(elevatorState => elevators.indexOf(elevatorState))
    maybeIndex.map { case index if index >= 0 =>
      elevators.updated(index, elevatorState)
    }
  }

  override def pickup(pickupRequest: PickupRequest): Unit = {
    if (!pickupRequests.contains(pickupRequest)) pickupRequests.+:(pickupRequest)
  }

  override def step(): Unit = {
    elevators.foreach { elevator =>
      val maybePickupRequest = pickupRequests.headOption
      maybePickupRequest match {
        case Some(pickupRequest) =>
          println(s"Sending a pickup request: {floor: ${pickupRequest.pickupFloor} ; direction: ${pickupRequest.direction} " +
            s"to the elevator with id ${elevator.elevatorId}")

          elevator.update(elevator.nextFloor(), pickupRequest.pickupFloor)
        case None if elevator.goalFloorNumbers.isEmpty =>
          println(s"We have nothing to do, no pickup request and no goals available for elevator with id: ${elevator.elevatorId}")
        case _ =>
          println(s"Going to the next goal command for elevator with id: ${elevator.elevatorId}")
          elevator.update(elevator.nextFloor(), elevator.floorNumber)
      }
    }
  }
}