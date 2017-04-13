package ElevatorControlSystem

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually
import scala.collection.immutable.Queue

class PickupRequestQueueTest extends WordSpec with MockitoSugar with Eventually with Matchers {
  "The PickupRequestQueue" when {
    "calling the enqueue method" should {
      "will enqueue the pickupRequest in the queue" in {
        val queueMock = mock[Queue[PickupRequest]]
        val pickupRequest = PickupRequest(3, 5)
        when(queueMock.enqueue(pickupRequest)).thenReturn(queueMock)
        val pickupRequestQueue = new PickupRequestQueue(queueMock)

        pickupRequestQueue.enqueue(pickupRequest)

        verify(queueMock).enqueue(pickupRequest)
      }
    }

    "calling the dequeue method" should {
      "call the queue dequeue method and return the updated queue and the optional pickupRequest" in {
        val queueMock = mock[Queue[PickupRequest]]
        val queueAfterEnqueueMock = mock[Queue[PickupRequest]]
        val pickupRequest = PickupRequest(3, 5)

        when(queueMock.enqueue(pickupRequest)).thenReturn(queueAfterEnqueueMock)
        when(queueMock.dequeueOption).thenReturn(Some((pickupRequest, queueMock)))

        queueMock.dequeueOption should be(Some(pickupRequest, queueMock))
      }
    }

    "calling the dequeue method when queue is empty" should {
      "call the queue dequeue method and return nothing" in {
        val queueMock = mock[Queue[PickupRequest]]

        when(queueMock.dequeueOption).thenReturn(None)

        queueMock.dequeueOption should be(None)
      }
    }
  }
}
