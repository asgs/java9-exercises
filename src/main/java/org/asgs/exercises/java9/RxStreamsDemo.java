package org.asgs.exercises.java9;

import org.asgs.exercises.java9.publisher.SimplePublisher;
import org.asgs.exercises.java9.subscriber.SimpleSubscriber;

import java.util.concurrent.SubmissionPublisher;
import java.util.stream.IntStream;

/** A Simple program to demo the RX capabilities of Java 9 Flow API. */
public class RxStreamsDemo {
  public static void main(String[] args) {
    simplePubSub();
    submissionPubSub();
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
   * Stream events to subscribers using a <code>SimplePublisher</code> that doesn't **comply** to
   * the <code>Flow.Subscription</code> semantics.
   */
  private static void simplePubSub() {
    SimplePublisher<Integer> publisher = new SimplePublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 100).forEach(publisher::submit);
    publisher.finish();
  }
}
