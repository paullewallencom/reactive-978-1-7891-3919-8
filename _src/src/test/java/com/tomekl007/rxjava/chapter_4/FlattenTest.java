package com.tomekl007.rxjava.chapter_4;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FlattenTest {

  @Test
  public void whenTransformationProduceMultipleEvents_thenCreateObservableOfObservablesStructure(){
    //given
    Observable<Integer> obs = Observable.fromArray(1,2,3);
    List<Integer> res = new ArrayList<>();

    //when
    obs
        .flatMap(this::rangeFromToTen) //flatten from Observable<Observable<Integer>> to Observable<Integer>
        .filter(v -> v >= 8) // filter is easy to do because we are handling on list structure
        .collect(() -> res, List::add)
        .blockingGet();

    //then
    assertThat(res).containsExactly(8, 9, 10, 8, 9, 10, 11, 8, 9, 10, 11, 12);
  }

  private Observable<Integer> rangeFromToTen(Integer i) {
    return Observable.range(i, 10);
  }



  @Test
  public void whenFlatMapped_thenCompletedSuccessfully() {
    Completable allElementsCompletable = Flowable.just("some request", "user is doing actiokn")
        .flatMapCompletable(message -> Completable
            .fromRunnable(() -> System.out.println(message))
        );
    allElementsCompletable
        .test()
        .assertComplete();
  }

}
