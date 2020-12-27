package com.tomekl007.rxjava.chapter_3;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

public class GroupByTest {

  @Test
  public void shouldUseGroupByToGroupAllElementsByKey() {
    //given
    Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    //when
    Disposable subscribe = Observable.fromArray(numbers)
        .groupBy(i -> 0 == (i % 2) ? "EVEN" : "ODD")
        .flatMapMaybe(group -> group.reduce((a, b) -> a + b)
            .map(v -> "Group " + group.getKey() + " sum is " + v))
        .subscribe(System.out::println);

    //then
  }
}
