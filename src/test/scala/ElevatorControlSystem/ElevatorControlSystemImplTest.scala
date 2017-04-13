package ElevatorControlSystem

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually

class ElevatorControlSystemImplTest extends WordSpec with Matchers with MockitoSugar with Eventually {

  "The ElevatorControlSystem" when {
    "Created given a numberOfElevators > 16" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "return a validation error" in {
        val failure = intercept[IllegalArgumentException] {
          new ElevatorControlSystemImpl(21, 6)
        }
        failure.getMessage should be("requirement failed: numberOfElevators: 21 argument must be between 0 - 16")
      }
    }

    "Calling the update method" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "return the ElevatorStates for the system" in {
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        val elevatorState = ElevatorState(elevatorId = 1, floorNumber = 5, floors = 6, goalFloorNumbers = Set(5))
        elevatorControlSystem.update(elevatorState, 5)
        elevatorControlSystem.status().head should be(elevatorState)
      }
    }

    "Calling the update method for an elevator state that does not exist" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "return the old elevator state for the system" in {
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        val elevatorState = ElevatorState(elevatorId = 5, floorNumber = 5, floors = 6, goalFloorNumbers = Set(5))
        elevatorControlSystem.update(elevatorState, 5)
        elevatorControlSystem.status().head should not be elevatorState
      }
    }

    "Calling the update method for a system with no elevators" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "return no ElevatorStates for the system" in {
        val elevatorControlSystem = new ElevatorControlSystemImpl(0, 6)
        val elevatorState = ElevatorState(elevatorId = 5, floorNumber = 5, floors = 6, goalFloorNumbers = Set(5))
        elevatorControlSystem.update(elevatorState, 5)
        elevatorControlSystem.status() should be(Seq.empty)
      }
    }

    "Calling the status method" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "return the elevators with the given elevator updated" in {
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        elevatorControlSystem.status() should be(Seq(ElevatorState(1, 0, 6, Set.empty), ElevatorState(2, 0, 6, Set.empty)))
      }
    }

    "Calling the pickup method" should {
      implicit val pickupRequestQueueMock = mock[PickupRequestQueue]

      "verify that the pickupRequest is enqueued in the queue" in {
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        val pickupRequest = PickupRequest(pickupFloor = 3, 1)
        elevatorControlSystem.pickup(pickupRequest)

        verify(pickupRequestQueueMock).enqueue(pickupRequest)
      }
    }

    "Calling the step method" should {
      "verify that the dequeue method is called for each elevator" in {
        implicit val pickupRequestQueueMock = mock[PickupRequestQueue]
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)

        elevatorControlSystem.step()

        verify(pickupRequestQueueMock, times(2)).dequeue()
      }
    }

    "Calling the step method given the queue is not empty" should {
      "update the elevator state with the dequeued command" in {
        implicit val pickupRequestQueueMock = mock[PickupRequestQueue]
        when(pickupRequestQueueMock.dequeue()).thenReturn(Some(PickupRequest(4, 2)))
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)

        elevatorControlSystem.step()

        elevatorControlSystem.status().head should be(ElevatorState(1, 1, 6, Set(4)))
      }
    }

    "Calling the step method given the queue is empty and there is an elevator with a goal set" should {
      "update the elevator state by executing step" in {
        implicit val pickupRequestQueueMock = mock[PickupRequestQueue]
        when(pickupRequestQueueMock.dequeue()).thenReturn(None)
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        elevatorControlSystem.update(elevatorControlSystem.status().head, 3)

        elevatorControlSystem.step()

        elevatorControlSystem.status().head should be(ElevatorState(1, 2, 6, Set(3)))
      }
    }

    "Calling the step method given the queue is empty and there is no elevator with a goal set" should {
      "update the elevator state by executing step" in {
        implicit val pickupRequestQueueMock = mock[PickupRequestQueue]
        when(pickupRequestQueueMock.dequeue()).thenReturn(None)
        val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)

        elevatorControlSystem.step()

        elevatorControlSystem.status().head should be(ElevatorState(1, 0, 6, Set.empty))
      }
    }
  }
}
