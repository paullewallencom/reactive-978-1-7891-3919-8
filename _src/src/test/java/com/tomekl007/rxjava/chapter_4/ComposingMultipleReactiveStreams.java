package com.tomekl007.rxjava.chapter_4;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableCompletableObserver;
import org.junit.Before;
import org.junit.Test;

public class ComposingMultipleReactiveStreams {

  private Completable first;
  private Completable second;
  private Completable error;
  private final Throwable throwable = new RuntimeException();

  @Before
  public void setUpCompletable() {
    first = Completable.fromSingle(Single.just(1));
    second = Completable.fromRunnable(() -> {
    });
    error = Single.error(throwable)
        .ignoreElement();
  }

  @Test
  public void whenCompletableConstructed_thenCompletedSuccessfully() {
    Completable completed = Completable.complete();
    completed.subscribe(new DisposableCompletableObserver() {
      @Override
      public void onComplete() {
        System.out.println("Completed!");
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }
    });
    Flowable<String> flowable = Flowable.just("request received", "user logged in");
    Completable flowableCompletable = Completable.fromPublisher(flowable);
    Completable singleCompletable = Single.just(1)
        .ignoreElement();

    completed.andThen(flowableCompletable)
        .andThen(singleCompletable)
        .test()
        .assertComplete();
  }

  @Test
  public void whenCombiningCompletables_thenCompletedSuccessfully() {
    first.andThen(second)
        .test()
        .assertComplete();
  }

  @Test
  public void whenCombinedWithError_thenCompletedWithError() {
    first.andThen(second)
        .andThen(error)
        .test()
        .assertError(throwable);
  }

  @Test
  public void whenCombinedWithNever_thenDoesNotComplete() {
    first.andThen(second)
        .andThen(Completable.never())
        .test()
        .assertNotComplete();
  }

  @Test
  public void whenMergedCompletables_thenCompletedSuccessfully() {
    Completable.mergeArray(first, second)
        .test()
        .assertComplete();
  }

  @Test
  public void whenMergedWithError_thenCompletedWithError() {
    Completable.mergeArray(first, second, error)
        .test()
        .assertError(throwable);
  }


  @Test
  public void whenAmbWithNever_thenCompletedSuccessfully() {
    Completable.ambArray(first, Completable.never(), second)
        .test()
        .assertComplete();
  }

  @Test
  public void whenAmbWithError_thenCompletedWithError() {
    Completable.ambArray(error, first, second)
        .test()
        .assertError(throwable);
  }


}
