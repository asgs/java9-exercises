package org.asgs.exercises.java9.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

/**
 * A simple publisher that registers subscribers and publishes to each of them whenever submit is
 * invoked. It uses parallelStream to publish the events, thereby using the default internal
 * executor JDK uses.
 */
public class SimplePublisher<T> implements Flow.Publisher<T> {

  private List<Flow.Subscriber<? super T>> subscribers = new ArrayList<>();

  @Override
  public void subscribe(Flow.Subscriber<? super T> subscriber) {
    subscribers.add(subscriber);
  }

  public void submit(T element) {
    subscribers.parallelStream().forEach(s -> s.onNext(element));
  }

  /** Signals all subscribers that they have reached the End of Stream. */
  public void finish() {
    subscribers.parallelStream().forEach(Flow.Subscriber::onComplete);
  }
}
