package com.tomekl007.rxjava.chapter_6;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class HotColdObservable {

  @Test
  public void shouldCreateColdObservable() throws InterruptedException {
    //given
    Observable<Long> cold = Observable.interval(200, TimeUnit.MILLISECONDS);

    cold.subscribe(i -> System.out.println("First: " + i));
    Thread.sleep(500);
    cold.subscribe(i -> System.out.println("Second: " + i));
    //same sequence will be generated for both subscribe calls

    Thread.sleep(5000);
  }

  @Test
  public void shouldConnectToColdObservable() throws InterruptedException {
    ConnectableObservable<Long> cold = Observable.interval(200, TimeUnit.MILLISECONDS).publish();
    cold.connect();

    cold.subscribe(i -> System.out.println("First: " + i));
    Thread.sleep(500);
    cold.subscribe(i -> System.out.println("Second: " + i));

    Thread.sleep(5000);
  }

  @Test
  public void whenDoingReconnectOnColdObservableTheSequenceWillBeRegeneratedFromTheBeginning() throws InterruptedException {
    ConnectableObservable<Long> connectable = Observable.interval(200, TimeUnit.MILLISECONDS).publish();
    Disposable s = connectable.connect();

    connectable.subscribe(i -> System.out.println(i));

    Thread.sleep(1000);
    System.out.println("Closing connection");
    s.dispose();
    Thread.sleep(2000);

    Thread.sleep(1000);
    System.out.println("Reconnecting");
    s = connectable.connect();
    connectable.subscribe(i -> System.out.println(i));
    Thread.sleep(2000);
  }

  @Test
  public void whenDoingReconnectOnHotObservableItWillPickUpWhenStoppedWithoutRegeneratingSequence() throws InterruptedException {
    ConnectableObservable<Long> connectable = Observable.interval(200, TimeUnit.MILLISECONDS).publish();
    Disposable s = connectable.connect();

    Disposable s1 = connectable.subscribe(i -> System.out.println("First: " + i));
    Thread.sleep(500);
    Disposable s2 = connectable.subscribe(i -> System.out.println("Second: " + i));

    Thread.sleep(500);
    System.out.println("Unsubscribing second");
    s2.dispose();
    connectable.subscribe(i -> System.out.println(i));
    Thread.sleep(2000);
  }

}
