package org.asgs.exercises.java9.subscriber;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitedConsumptionSubscriber<T> extends SimpleSubscriber<T> {
  private AtomicInteger counter = new AtomicInteger();
  private Integer MAX_COUNT = 89;

  @Override
  public void onNext(T item) {
    System.out.println("Item received " + item);
    Flow.Subscription subscription = getSubscription();
    if (subscription != null) {
      if (counter.incrementAndGet() >= MAX_COUNT) {
        subscription.cancel(); // This way the Publisher knows to produce more items, else it stops.
        System.out.println("Reached max count for subscription. Requested cancellation.");
      } else {
        subscription.request(
            1); // This way the Publisher knows to produce more items, else it stops.
        System.out.println("Requesting one more item.");
      }
    }
  }
}
