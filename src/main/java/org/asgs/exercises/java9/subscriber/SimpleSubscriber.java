package org.asgs.exercises.java9.subscriber;

import java.util.concurrent.Flow;

/** @ param <T> */
public class SimpleSubscriber<T> implements Flow.Subscriber<T> {

  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1); // Request one more and this happens forever.
    System.out.println("Received subscription. Requesting one item.");
  }

  @Override
  public void onNext(T item) {
    System.out.println("Item received " + item);
    if (subscription != null) {
      subscription.request(1); // This way the Publisher knows to produce more items, else it stops.
      System.out.println("Requesting one more item.");
    }
  }

  @Override
  public void onError(Throwable throwable) {
    System.err.println("Exception subscribing to content;" + throwable.getMessage());
    if (subscription != null) {
      subscription.cancel();
      System.out.println("Canceling subscription.");
    }
  }

  @Override
  public void onComplete() {
    System.out.println("completed subscribing to content.");
    if (subscription != null) {
      subscription.cancel();
      System.out.println("Canceling subscription.");
    }
  }
}
