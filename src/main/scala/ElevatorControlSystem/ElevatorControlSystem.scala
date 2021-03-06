package ElevatorControlSystem

case class PickupRequest(pickupFloor: Int, direction: Direction)

trait ElevatorControlSystem {
  def status(): Seq[ElevatorState]

  def update(elevatorStatus: ElevatorState): Unit

  def update(elevator: ElevatorState, goalFloor: Int): Unit

  def pickup(pickupRequest: PickupRequest): Unit

  def step(): Unit
}

class ElevatorControlSystemImpl(numberOfElevators: Int, numberOfFloors: Int)(implicit pickupRequestQueue: PickupRequestQueue) extends ElevatorControlSystem {

  require(numberOfElevators >= 0 && numberOfElevators <= 16, s"numberOfElevators: $numberOfElevators argument must be between 0 - 16")

  private var elevators = for (i <- 1 until numberOfElevators + 1) yield ElevatorState(i, 0, numberOfFloors, Set.empty)

  override def update(elevator: ElevatorState, goalFloor: Int): Unit = {
    val updatedElevatorState = elevator.update(elevator.nextFloor(goalFloor), goalFloor)
    val maybeUpdatedElevator = elevators.find(_.elevatorId == elevator.elevatorId)
    maybeUpdatedElevator.foreach(updatedElevator =>
      elevators = elevators.updated(elevators.indexOf(updatedElevator), updatedElevatorState))
  }

  override def status(): Seq[ElevatorState] = {
    elevators
  }

  override def update(elevatorState: ElevatorState): Unit = {
    val maybeElevatorState = elevators.find(elevator => elevator.elevatorId == elevatorState.elevatorId)
    val maybeIndex = maybeElevatorState.map(elevatorState => elevators.indexOf(elevatorState))
    maybeIndex.foreach { case index if index >= 0 =>
      elevators = elevators.updated(index, elevatorState)
    }
  }

  override def pickup(pickupRequest: PickupRequest): Unit = {
    pickupRequestQueue.enqueue(pickupRequest)
  }

  override def step(): Unit = {
    elevators.foreach { elevator =>
      val maybePickupRequest = pickupRequestQueue.dequeue()
      maybePickupRequest match {
        case Some(pickupRequest) =>
          println(s"Sending a pickup request: {floor: ${pickupRequest.pickupFloor} ; direction: ${pickupRequest.direction} " +
            s"to the elevator with id ${elevator.elevatorId}")

          update(elevator, pickupRequest.pickupFloor)
        case _ if elevator.goalFloorNumbers.isEmpty =>
          println(s"We have nothing to do, no pickup request and no goals available for elevator with id: ${elevator.elevatorId}")
        case _ =>
          println(s"Going to the next goal command for elevator with id: ${elevator.elevatorId}")
          val nextGoalFloor = elevator.goalFloorNumbers.headOption.getOrElse(elevator.floorNumber)
          update(elevator, nextGoalFloor)
      }
    }
  }
}

object ElevatorControlSystem {
  def printElevatorStates(elevatorStates: Seq[ElevatorState]): Unit = {
    println("Here is a summary of the elevator states:")
    elevatorStates.foreach { elevatorState =>
      println(s"Elevator with id: ${elevatorState.elevatorId} is at floor number: ${elevatorState.floorNumber} ")
      if (elevatorState.goalFloorNumbers.nonEmpty)
        println(s"and has the following goal floor numbers: ${elevatorState.goalFloorNumbers.mkString(", ")}")
    }
  }
}