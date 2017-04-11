package ElevatorControlSystem

import org.scalatest.{Matchers, WordSpec}

class ElevatorStateTest extends WordSpec with Matchers {
  "The ElevatorState" when {
    "Created with invalid arguments (floorNumber > floors)" should {
      "return a validation error" in {
        val exception = intercept[IllegalArgumentException] {
          ElevatorState(elevatorId = 1, floorNumber = 10, floors = 4, goalFloorNumbers = Set.empty)
        }

        exception.getMessage should be("requirement failed: floorNumber: 10 argument must be between 0 - 4 (building number of floors)")
      }
    }

    "Created with at least one invalid argument (goalFloor > floors)" should {
      "return a validation error" in {
        val exception = intercept[IllegalArgumentException] {
          ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(1, 2, 5))
        }

        exception.getMessage should be("requirement failed: goalFloor: 5 argument must be between 0 - 4 (building number of floors)")
      }
    }

    "Calling the update method with newFloorNumber and goalFloor arguments" should {
      "create a new ElevatorState with the updated newFloorNumber and goalFloor" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 2, floors = 4, goalFloorNumbers = Set.empty)
        }

        val updatedElevatorState = elevatorState.update(3, 4)

        updatedElevatorState should be(ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(4)))
      }
    }

    "Calling the update method with floorNumber greater then the number of floors" should {
      "return a validation error" in {
        val elevatorState = ElevatorState(elevatorId = 1, floorNumber = 2, floors = 4, goalFloorNumbers = Set.empty)

        val exception = intercept[java.lang.IllegalArgumentException] {
          elevatorState.update(newFloorNumber = 5, goalFloor = 4)
        }

        exception.getMessage should be("requirement failed: newFloorNumber: 5 argument must be between 0 - 4 (building number of floors)")
      }
    }

    "Calling the update method with goalFloor greater then the number of floors" should {
      "return a validation error" in {
        val elevatorState = ElevatorState(elevatorId = 1, floorNumber = 2, floors = 4, goalFloorNumbers = Set.empty)

        val exception = intercept[java.lang.IllegalArgumentException] {
          elevatorState.update(newFloorNumber = 1, goalFloor = 5)
        }

        exception.getMessage should be("requirement failed: goalFloor: 5 argument must be between 0 - 4 (building number of floors)")
      }
    }

    "Calling the update method with newFloorNumber equal to floorNumber" should {
      "create a new ElevatorState without the goalFloor equal to newFloorNumber as we reached a destination" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(3, 4))
        }

        val updatedElevatorState = elevatorState.update(3, 4)

        updatedElevatorState should be(ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(4)))

      }
    }

    "Calling the state method" should {
      "return the ElevatorState object" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(3, 4))
        }

        elevatorState.state() should be(elevatorState)
      }
    }

    "Calling the nextFloor method with a goalFloor = 4 and currentFloor = 3" should {
      "return floor number = 4 as the elevator must go up" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 3, floors = 4, goalFloorNumbers = Set(4))
        }

        elevatorState.nextFloor(4) should be(4)
      }
    }

    "Calling the nextFloor method with a goalFloor = 2 and currentFloor = 4" should {
      "return floor number = 3 as the elevator must go down" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 4, floors = 4, goalFloorNumbers = Set(2))
        }

        elevatorState.nextFloor(2) should be(3)
      }
    }

    "Calling the nextFloor method with a goalFloor = 2 and currentFloor = 2" should {
      "return floor number = 2 as the elevator is already at destination floor" in {
        val elevatorState = {
          ElevatorState(elevatorId = 1, floorNumber = 2, floors = 4, goalFloorNumbers = Set(2))
        }

        elevatorState.nextFloor(2) should be(2)
      }
    }
  }
}