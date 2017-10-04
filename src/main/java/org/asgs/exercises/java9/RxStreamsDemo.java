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

  private static void submissionPubSub() {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 100).forEach(publisher::submit);
    publisher.close();
  }

  private static void simplePubSub() {
    SimplePublisher<Integer> publisher = new SimplePublisher<>();
    SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
    publisher.subscribe(subscriber);
    IntStream.rangeClosed(1, 100).forEach(publisher::submit);
    publisher.finish();
  }
}
