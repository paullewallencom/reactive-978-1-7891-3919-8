package com.tomekl007.rxjava.chapter_4;

import io.reactivex.Observable;
import org.junit.Test;

public class MultipleEventsTest {


  @Test
  public void whenTransformationProduceMultipleEvents_thenCreateObservableOfObservablesStructure(){
    //given
    Observable<Integer> obs = Observable.fromArray(1,2,3);

    //when
    obs
        .map(this::rangeFromToTen);
//        .filter(f -> f.filter(v -> v >= 8).blockingFirst()); how to filter Observable<Observable<Integer>> ???
  }

  private Observable<Integer> rangeFromToTen(Integer i) {
    return Observable.range(i, 10);
  }


}
