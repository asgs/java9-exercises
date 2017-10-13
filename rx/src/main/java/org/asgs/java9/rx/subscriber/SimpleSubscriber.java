package org.asgs.java9.rx.subscriber;

import java.util.concurrent.Flow;

/** @ param <T> */
public class SimpleSubscriber<T> implements Flow.Subscriber<T> {

  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1); // Request one more and this happens forever.
    System.out.println("Received subscription. Requesting one item on " + this.hashCode());
  }

  @Override
  public void onNext(T item) {
    System.out.println("Item received " + item + " on subscriber " + this.hashCode());
    if (subscription != null) {
      subscription.request(1); // This way the Publisher knows to produce more items, else it stops.
      System.out.println("Requesting one more item on subscriber " + this.hashCode());
    }
  }

  @Override
  public void onError(Throwable throwable) {
    System.err.println(
        "Exception subscribing to content on subscriber "
            + this.hashCode()
            + "; "
            + throwable.getMessage());
    if (subscription != null) {
      subscription.cancel();
      System.out.println("Canceling subscription on subscriber " + this.hashCode());
    }
  }

  @Override
  public void onComplete() {
    System.out.println("completed subscribing to content on subscriber " + this.hashCode());
    if (subscription != null) {
      subscription.cancel();
      System.out.println("Closing subscription on subscriber " + this.hashCode());
    }
  }

  public Flow.Subscription getSubscription() {
    return subscription;
  }
}
