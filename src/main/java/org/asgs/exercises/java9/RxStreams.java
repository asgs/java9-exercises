package org.asgs.exercises.java9;

import org.asgs.exercises.java9.publisher.SimplePublisher;
import org.asgs.exercises.java9.subscriber.SimpleSubscriber;

import java.util.stream.IntStream;

/** Hello world! */
public class RxStreams {
  public static void main(String[] args) {
      SimplePublisher<Integer> publisher = new SimplePublisher<>();
      SimpleSubscriber<Integer> subscriber = new SimpleSubscriber<>();
      publisher.subscribe(subscriber);
      IntStream.rangeClosed(1, 100).forEach(publisher::submit);
      publisher.finish();
  }
}
