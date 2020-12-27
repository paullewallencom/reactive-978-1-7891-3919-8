package com.tomekl007.rxjava.chapter_1;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ReactiveIO {
  @Test
  public void shouldObserveOnComputationScheduler() {
    //given
    List<Integer> expected = IntStream.range(0, 100000).boxed().collect(Collectors.toList());
    Observable<Integer> observable = Observable.fromIterable(expected);

    //when
    TestSubscriber<Integer> testSubscriber =
        observable.toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.computation()).test();
    testSubscriber.awaitTerminalEvent();

    //then
    List<Integer> receivedInts = testSubscriber
        .getEvents().get(0).stream().mapToInt(object -> (int) object).boxed().collect(Collectors.toList());

    assertEquals(expected, receivedInts);
  }

  @Test
  public void shouldObserveOnIOSchedulerForBlockingIOOperations() {
    //given
    List<Integer> expected = Collections.singletonList(100);
    Callable<Integer> ioOperation = executeBlockingIOOperation();
    Observable<Integer> observable = Observable.fromCallable(ioOperation);

    //when
    TestSubscriber<Integer> testSubscriber =
        observable
            .toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.io()).test();
    testSubscriber.awaitTerminalEvent();

    //then
    List<Integer> receivedInts = testSubscriber
        .getEvents().get(0).stream().mapToInt(object -> (int) object).boxed().collect(Collectors.toList());

    assertEquals(expected, receivedInts);
  }

  private Callable<Integer> executeBlockingIOOperation() {
    return () -> {
      System.out.println("executing I/O operation");
      try {
        Thread.sleep(1000);//simulate IO delay
        return 100;
      } catch (InterruptedException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    };
  }


}
