package com.tomekl007.rxjava.chapter_3;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MapTest {
  @Test
  public void shouldMapResults_andGetOnlyFirst_whenBlockingGet() throws InterruptedException {
    AtomicInteger emittedTotal = new AtomicInteger();

    Integer result = Observable.range(1, 5)
        .map(i -> i * 100)
        .doOnNext(i -> {
          emittedTotal.addAndGet(i);
          System.out.println("Emitting " + i
              + " on thread " + Thread.currentThread().getName());
        })
        .map(i -> i * 10)
        .doOnNext(i -> {
          emittedTotal.addAndGet(i);
          System.out.println("Received " + i + " on thread "
              + Thread.currentThread().getName());
        })
        .firstElement()
        .blockingGet();

    assertThat(result).isEqualTo(1000);
    assertThat(emittedTotal.get()).isEqualTo(1100);
  }

  @Test
  public void shouldMapResults_andGetOnlyLast_whenBlockingLast() throws InterruptedException {
    AtomicInteger emittedTotal = new AtomicInteger();

    Integer result = Observable.range(1, 5)
        .map(i -> i * 100)
        .doOnNext(i -> {
          emittedTotal.addAndGet(i);
          System.out.println("Emitting " + i
              + " on thread " + Thread.currentThread().getName());
        })
        .map(i -> i * 10)
        .doOnNext(i -> {
          emittedTotal.addAndGet(i);
          System.out.println("Received " + i + " on thread "
              + Thread.currentThread().getName());
        })
        .blockingLast();

    assertThat(result).isEqualTo(5000);
    assertThat(emittedTotal.get()).isEqualTo(16500);
  }
}
