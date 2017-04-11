package ElevatorControlSystem

import org.scalatest.{Matchers, WordSpec}

class ElevatorControlSystemImplTest extends WordSpec with Matchers {

  "The ElevatorControlSystem" when {
    "Created given a numberOfElevators > 16" should {
      "return a validation error" in {
        val failure = intercept[IllegalArgumentException] {
          new ElevatorControlSystemImpl(21, 6)
        }
        failure.getMessage should be("requirement failed: numberOfElevators: 21 argument must be between 0 - 16")
      }
    }

    "Calling the status method" should {
      "return the ElevatorStates for the system" in {
         val elevatorControlSystem = new ElevatorControlSystemImpl(2, 6)
        elevatorControlSystem.status() should be(Seq(ElevatorState(1, 0, 6, Set.empty), ElevatorState(2, 0, 6, Set.empty)))
      }
    }
  }
}
