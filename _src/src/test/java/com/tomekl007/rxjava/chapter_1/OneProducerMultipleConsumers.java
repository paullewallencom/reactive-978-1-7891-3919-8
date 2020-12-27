package com.tomekl007.rxjava.chapter_1;

import com.tomekl007.reactive.com.baeldung.rxjava.chapter_1.SubjectsProvider;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OneProducerMultipleConsumers {

  @Test
  public void givenOneProducer_whenUsingSubject_shouldSendEventsToTwoObservables() {
    PublishSubject<Integer> subject = PublishSubject.create();

    subject.subscribe(SubjectsProvider.firstObserver());
    subject.onNext(1);
    subject.onNext(2);
    subject.onNext(3);

    subject.subscribe(SubjectsProvider.secondObserver());
    subject.onNext(4); //4 will be retrieved by both subscribers
    subject.onComplete();

    assertEquals(14, SubjectsProvider.subscriber1 + SubjectsProvider.subscriber2);
  }
}
