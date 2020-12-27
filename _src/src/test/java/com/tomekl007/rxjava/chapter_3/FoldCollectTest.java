package com.tomekl007.rxjava.chapter_3;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FoldCollectTest {

  @Test
  public void shouldCollectResults_usingCollectMethod() {
    ArrayList<Integer> results = new ArrayList<>();

    ArrayList<Integer> resultSingle = Observable.range(1, 5)
        .map(i -> i * 100)
        .doOnNext(i -> System.out.println("Emitting " + i
            + " on thread " + Thread.currentThread().getName()))
        .map(i -> i * 10)
        .doOnNext(i -> System.out.println("Received " + i + " on thread "
            + Thread.currentThread().getName()))
        .collect(() -> results, ArrayList::add).blockingGet();


    assertThat(resultSingle).containsExactly(1000, 2000, 3000, 4000, 5000);
    int result = resultSingle.stream().mapToInt(v -> v).sum();
    assertThat(result).isEqualTo(15000);
  }
  @Test
  public void usingCollectMethod_whenDoingBlockingGetTwoTimes_willExecuteLogicTwice() {
    ArrayList<Integer> results = new ArrayList<>();

    Single<ArrayList<Integer>> resultSingle = Observable.range(1, 5)
        .map(i -> i * 100)
        .doOnNext(i -> System.out.println("Emitting " + i
            + " on thread " + Thread.currentThread().getName()))
        .map(i -> i * 10)
        .doOnNext(i -> System.out.println("Received " + i + " on thread "
            + Thread.currentThread().getName()))
        .collect(() -> results, ArrayList::add);


    assertThat(resultSingle.blockingGet()).containsExactly(1000, 2000, 3000, 4000, 5000);
    int result = resultSingle.blockingGet().stream().mapToInt(v -> v).sum();
    assertThat(result).isEqualTo(30000);
  }
}
