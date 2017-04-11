package example

import ElevatorControlSystem.{ElevatorControlSystemImpl, PickupRequest}
import ElevatorControlSystem._

object App {
  def main(args: Array[String]) {
    println("Welcome to the Elevator control system simulation app!")
    val sc = new java.util.Scanner(System.in)
    println("Please provide the desired number of elevators: ")
    val numberOfElevators = sc.nextInt()

    println("Please provide the desired number of elevators: ")
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
          ElevatorControlSystem.printElevatorsStatus(elevatorControlSystem.status())
          println("Please choose your next option!")
        case "step" => elevatorControlSystem.step()
          println("Please choose your next option!")
        case "pickup" =>
          println("Please give your pickup floor number: ")
          val pickupFloor = sc.nextInt()
          println("Please give your direction (<0 -> down ; >0 -> up): ")
          val direction = sc.nextInt()
          val pickupRequest = PickupRequest(pickupFloor, direction)
          elevatorControlSystem.pickup(pickupRequest)
          println("Please choose your next option!")
      }
    }
  }
}