package org.asgs.exercises.java9;

import org.asgs.exercises.java9.publisher.SimplePublisher;
import org.asgs.exercises.java9.subscriber.LimitedConsumptionSubscriber;
import org.asgs.exercises.java9.subscriber.SimpleSubscriber;

import java.util.concurrent.SubmissionPublisher;
import java.util.stream.IntStream;

/** A Simple program to demo the Reactive Stream capabilities of Java 9 Flow API. */
public class RxStreamsDemo {
  public static void main(String[] args) {
    submissionPubSub();
    submissionPubSubWithErrors();
    submissionPubSubWithSubscriptionLimits();
    simplePubSub();
  }

  /**
   * Stream events to subscribers using a <code>SubmissionPublisher</code> that comes with JDK 9.
   */
  private static void submissionPubSub() {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 100).forEach(publisher::submit);
    publisher.close();
  }

  /**
   * Stream events to subscribers using a <code>SubmissionPublisher</code> that errors out after
   * streaming a few events.
   */
  private static void submissionPubSubWithErrors() {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 10).forEach(publisher::submit);
    publisher.closeExceptionally(new RuntimeException("Premature End of stream"));
  }

  /**
   * Stream events to subscribers that have limited subscription to events the <code>SimplePublisher
   * </code> publishes.
   */
  private static void submissionPubSubWithSubscriptionLimits() {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    LimitedConsumptionSubscriber<Integer> subscriber = new LimitedConsumptionSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 90).forEach(publisher::submit);
    publisher.close();
  }

  /**
   * Stream events to subscribers using a <code>SimplePublisher</code> that doesn't **comply** to
   * the <code>Flow.Subscription</code> semantics. This probably serves as an example of how NOT to
   * do Reactive programming.
   */
  private static void simplePubSub() {
    SimplePublisher<Integer> publisher = new SimplePublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 100).forEach(publisher::submit);
    publisher.finish();
  }
}
