ElevatorControlSystem
=================================
An elevator control system app. Application is built as a simple Scala console app.
No framework used.

#Compile, test & run instructions
In order to compile the app you should run from the app root folder: 

```sbt compile```

```sbt test```

```sbt run```

After starting the application you can play the elevator simulation by using these commands (typing them in)
 * pickup
 * status
 * step
 * quit
 
Description of the simulation is explained also during runtime.
 
#Data structures, algorithms & interfaces used

The system is created using these classes: 
* ElevatorControlSystem - main logic
* ElevatorState - describes one elevator state
* PickupRequest
* Direction - type safe class for direction

The list of pickup requests that the elevator control system must schedule is stored in a queue.
I decided to use a standard Scala immutable implementation of the queue. 