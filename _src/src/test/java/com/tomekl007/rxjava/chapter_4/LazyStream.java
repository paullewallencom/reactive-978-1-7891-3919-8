package com.tomekl007.rxjava.chapter_4;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import org.junit.Test;

public class LazyStream {

  @Test
  public void shouldDeferExecutionUnitRequested() {
    //given
    Integer[] letters = {1, 2, 3, 4, 5, 6};
    Observable<Integer> observable = Observable.fromArray(letters);

    //when
    Observable<Integer> observableResult = observable.map(i -> i * 2).filter(v -> v > 2);
    //observableResult has no subscriber so execution is not starting

    //then
    System.out.println("lazy - execution not started");

    observableResult.subscribe(
        integer -> System.out.println("consumer: " + integer));
    //execution started because first subsription
  }
}
