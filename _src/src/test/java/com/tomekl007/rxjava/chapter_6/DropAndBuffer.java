package com.tomekl007.rxjava.chapter_6;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class DropAndBuffer {

  @Test
  public void shouldBufferElements() throws InterruptedException {
    PublishSubject<Integer> source = PublishSubject.create();

    //Defining a buffer with a size of 1024 will give an Observer some time to catch up to an overproducing source.
    //The buffer will store items that were not yet processed.
    Disposable subscribe = source.buffer(1024)
        .observeOn(Schedulers.computation())
        .subscribe(ComputeFunction::compute, Throwable::printStackTrace);


    IntStream.range(1, 1_000_000).forEach(source::onNext);

    Thread.sleep(10_000);
    subscribe.dispose();
  }


  @Test
  public void shouldSkipSomeElements() throws InterruptedException {
    PublishSubject<Integer> source = PublishSubject.create();

    //The sample() method periodically looks into the sequence of elements and emits the last item
    //that was produced within the duration specified as a parameter
    Disposable subscribe = source.sample(100, TimeUnit.MILLISECONDS)
        .observeOn(Schedulers.computation())
        .subscribe(ComputeFunction::compute, Throwable::printStackTrace);


    IntStream.range(1, 1_000_000).forEach(source::onNext);

    Thread.sleep(10_000);
    subscribe.dispose();
  }
}
