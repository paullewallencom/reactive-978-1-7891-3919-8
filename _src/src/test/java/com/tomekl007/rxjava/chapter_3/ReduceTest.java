package com.tomekl007.rxjava.chapter_3;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ReduceTest {

  @Test
  public void shouldCollectResults_usingCollectMethod() throws InterruptedException {
    Single<Integer> reduce = Observable.range(1, 5)
        .map(i -> i * 100)
        .doOnNext(i -> System.out.println("Emitting " + i
            + " on thread " + Thread.currentThread().getName()))
        .map(i -> i * 10)
        .doOnNext(i -> System.out.println("Received " + i + " on thread "
            + Thread.currentThread().getName()))
        .reduce(0, new BiFunction<Integer, Integer, Integer>() {
          @Override
          public Integer apply(Integer accumulator, Integer integer) throws Exception {
            System.out.println(accumulator + " - " + integer);
            return accumulator + integer;
          }
        });

    assertThat(reduce.blockingGet()).isEqualTo(15000);
  }
}
