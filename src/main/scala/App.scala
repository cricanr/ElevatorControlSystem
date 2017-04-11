package example

import ElevatorControlSystem.{ElevatorControlSystemImpl, PickupRequest}
import ElevatorControlSystem._
import scala.util.{Failure, Try}

object App {
  def main(args: Array[String]) {
    val trySimulate = Try {
      simulate
    }
    trySimulate match {
      case Failure(throwable) =>
        println(s"Failure occurred: ${throwable.getClass} : ${throwable.getMessage}. Restarting simulation.")
        simulate
      case _ =>
    }
  }

  private def simulate = {
    println("Welcome to the Elevator control system simulation app!")
    val sc = new java.util.Scanner(System.in)
    println("Please provide the desired number of elevators (max 16): ")
    val numberOfElevators = sc.nextInt()
    require(numberOfElevators <= 16, s"Please give a number of elevators in the range 0 - 16")

    println("Please provide the desired number of floors the building has: ")
    val numberOfFloors = sc.nextInt()

    val elevatorControlSystem = new ElevatorControlSystemImpl(numberOfElevators, numberOfFloors)

    println("Please choose your option. Here are the commands possible: ")
    println(" ------ to quit simulation type in 'quit'                                                      ------")
    println(" ------ to get status of the elevator control system for all elevators please type in 'status' ------")
    println(" ------ to trigger a step of the elevator control system please type in 'step'                 ------")
    println(" ------ to fire a pickup request for the elevator control system please type in 'pickup'       ------")
    println(" ------ followed by 2 ints: goal floor and direction (<0 - means go down ; >0 - means go up'   ------")
    println(" ----------------------------------------------------------------------------------------------------")

    while (true) {
      val option = sc.next()
      println(s"Your selected option is: $option")

      option match {
        case "quit" =>
          println(" ----------------------------------------------------------------------------------------------------")
          println("Thanks for playing. Hope to see you soon, bye!")
          System.exit(1)
        case "status" =>
          ElevatorControlSystem.printElevatorStates(elevatorControlSystem.status())
          println("Please choose your next option!")
        case "step" => elevatorControlSystem.step()
          println("Please choose your next option!")
        case "pickup" =>
          println(s"Please give your pickup floor number (must be in the range: 0 - $numberOfFloors) ")

          val pickupFloor = sc.nextInt
          require(pickupFloor >= 0 && pickupFloor <= numberOfFloors,
            s"Please give your pickup floor number (must be in the range: 0 - $numberOfFloors)")

          println("Please give your direction (<0 -> down ; >0 -> up): ")
          val direction = sc.nextInt()
          val pickupRequest = PickupRequest(pickupFloor, direction)
          elevatorControlSystem.pickup(pickupRequest)
          println("Please choose your next option!")
        case _ =>
          println("Unknown option, please use a valid one (see above description) to continue simulation.")
      }
    }
  }
}