package ElevatorControlSystem

import scala.collection.immutable.Queue

class QueuePickupRequest {
  private var queue = Queue[PickupRequest]()

  def enqueue(pickupRequest: PickupRequest) = {
    if (!queue.contains(pickupRequest))
      queue = queue.enqueue(pickupRequest)
  }

  def dequeue(): Option[PickupRequest] = {
    val maybePickupRequest = queue.dequeueOption
    maybePickupRequest match {
      case Some((pickupRequest, updatedQueue)) =>
        queue = updatedQueue
        Some(pickupRequest)
      case _ => None
    }
  }
}