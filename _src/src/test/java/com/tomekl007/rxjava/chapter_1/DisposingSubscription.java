package com.tomekl007.rxjava.chapter_1;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DisposingSubscription {
  private String result = "";
  @Test
  public void givenString_whenJustAndSubscribe_thenEmitsSingleItem() {

    Observable<String> observable = Observable.just("Hello");
    Disposable subscribe = observable.subscribe(s -> result = s);
    assertEquals("Hello", result);
    subscribe.dispose();
  }

  @Test
  public void givenArray_whenFromAndSubscribe_thenEmitsItems() {
    String[] numbers = {"1", "2", "3", "4", "5", "6", "7"};
    Observable<String> observable = Observable.fromArray(numbers);
    Disposable subscribe = observable.subscribe(
        i -> result += i,
        Throwable::printStackTrace,
        () -> result += "_Complete"
    );
    assertEquals("1234567_Complete", result);
    subscribe.dispose();
  }

  @Test
  public void givenObservable_whenConvertToBlockingObservable_thenReturnFirstElement() {
    String[] letters = {"1", "2", "3", "4", "5", "6", "7"};
    Observable<String> observable = Observable.fromArray(letters);
    String blockingObservable = observable.firstElement().blockingGet();

    Disposable subscribe = observable.subscribe(
        i -> result += i,
        Throwable::printStackTrace,
        () -> result += "_Completed"
    );
    assertEquals(String.valueOf(result.charAt(0)), blockingObservable);
    subscribe.dispose();
  }
}
