package com.tomekl007.rxjava.chapter_6;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;

import java.util.stream.IntStream;

public class OverProducingObservable {

  @Test
  public void overProducingObservable() throws InterruptedException {
    PublishSubject<Integer> source = PublishSubject.create();

    Disposable subscribe = source.observeOn(Schedulers.computation())
        .subscribe(this::computeElement, Throwable::printStackTrace);

    IntStream.range(1, 1_000_000).forEach(source::onNext);

    Thread.sleep(10_000);
    subscribe.dispose();

  }

  private void computeElement(Integer v) {
    System.out.println("Expensive consumer operation for value v: " + v);
    try {
      //simulate some delay
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
