package ElevatorControlSystem

import scala.collection.immutable.Queue

case class PickupRequest(pickupFloor: Int, direction: Int)

trait ElevatorControlSystem {
  def status(): Seq[ElevatorState]

  def update(elevatorStatus: ElevatorState): Unit

  def pickup(pickupRequest: PickupRequest): Unit

  def step(): Unit
}

class ElevatorControlSystemImpl(numberOfElevators: Int, numberOfFloors: Int) extends ElevatorControlSystem {
  private var elevators = for (i <- 0 until numberOfElevators) yield ElevatorState(i, 0, numberOfFloors, Set.empty)
  private var pickupRequests = Queue[PickupRequest]()

  private def update(elevator: ElevatorState, goalFloor: Int): Unit = {
    val updatedElevatorState = elevator.update(elevator.nextFloor(goalFloor), goalFloor)
    elevators = elevators.updated(elevators.indexOf(elevator), updatedElevatorState)
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
    if (!pickupRequests.contains(pickupRequest))
      pickupRequests = pickupRequests.enqueue(pickupRequest)
  }

  override def step(): Unit = {
    elevators.foreach { elevator =>
      val maybePickupRequest = pickupRequests.dequeueOption
      maybePickupRequest match {
        case Some((pickupRequest, updatedQueue)) =>
          pickupRequests = updatedQueue
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
  def printElevatorsStatus(elevatorStates: Seq[ElevatorState]): Unit = {
    println("Here is a summary of the elevator states:")
    elevatorStates.foreach { elevatorState =>
      println(s"Elevator with id: ${elevatorState.elevatorId} is at floor number: ${elevatorState.floorNumber} ")
      if (elevatorState.goalFloorNumbers.nonEmpty)
        println(s"and has the following goal floor numbers: ${elevatorState.goalFloorNumbers.mkString(", ")}")
    }
  }
}