package org.asgs.exercises.java9.subscriber;

import java.util.concurrent.Flow;

/** @ param <T> */
public class SimpleSubscriber<T> implements Flow.Subscriber<T> {

  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1); // Request one more and this happens forever.
  }

  @Override
  public void onNext(T item) {
    System.out.println("Item received " + item);
    subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    System.err.println("Exception subscribing to content;" + throwable.getMessage());
    subscription.cancel();
  }

  @Override
  public void onComplete() {
    System.out.println("completed subscribing to content.");
    subscription.cancel();
  }
}
