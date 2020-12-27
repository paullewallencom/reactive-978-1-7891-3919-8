package com.tomekl007.rxjava.chapter_5;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SlidingWindowTest {

  @Test
  public void givenStreamOfActivity_whenGroupByTime_thenShouldFinish() {
    //given
    final TestScheduler scheduler = new TestScheduler();
    List<WindowOperatorTest.Activity> activities = Arrays.asList(
        WindowOperatorTest.Activity.newInstance("u1", true),
        WindowOperatorTest.Activity.newInstance("u1", false),
        WindowOperatorTest.Activity.newInstance("u2", true),
        WindowOperatorTest.Activity.newInstance("u3", false)
    );

    //when
    Observable.fromIterable(activities)
        .window(5, TimeUnit.SECONDS, scheduler)
        .subscribe(activityObservable -> activityObservable.map(a -> a).subscribe(System.out::println));

    scheduler.advanceTimeBy(10, TimeUnit.SECONDS);
  }

  @Test
  public void slidingWindow() {
    Flowable<Data> events = Flowable.just(
        new Data(5, 1.0),
        new Data(10, 1.5),
        new Data(30, 2.8),
        new Data(40, 3.2),
        new Data(60, 3.8),
        new Data(90, 4.2)).observeOn(Schedulers.io());

    List<List<Data>> windows = window(3, 1, events).toList().blockingGet();
    System.out.println(windows);
    assertThat(windows).isNotNull();
    assertThat(windows).isNotEmpty();

    assertThat(windows.size()).isEqualTo(5);
    assertThat(windows.get(0)).containsExactly(new Data(5, 1.0), new Data(10, 1.5), new Data(30, 2.8));
    assertThat(windows.get(1)).containsExactly(new Data(10, 1.5), new Data(30, 2.8),new Data(40, 3.2), new Data(60, 3.8));
    assertThat(windows.get(2)).containsExactly(new Data(30, 2.8),new Data(40, 3.2), new Data(60, 3.8), new Data(90, 4.2));
    assertThat(windows.get(3)).containsExactly(new Data(40, 3.2), new Data(60, 3.8), new Data(90, 4.2));
    assertThat(windows.get(4)).containsExactly(new Data(90, 4.2));
  }

  private Flowable<List<Data>> window(Integer windowSize, Integer slideSize, Flowable<Data> data) {
    return window(0,
        windowSize,
        slideSize,
        data);
  }

  private Flowable<List<Data>> window(int from, Integer to, Integer slideSize, Flowable<Data> data) {
    Flowable<Data> window = data.takeWhile(it -> it.time <= to).skipWhile(it -> it.time < from);
    Flowable<Data> tail = data.skipWhile(it -> it.time <= from + slideSize);
    Maybe<List<Data>> nonEmptyWindow = window.toList().filter(it -> !it.isEmpty());
    Flowable<List<Data>> nextWindow = nonEmptyWindow
        .flatMapPublisher(integers -> window(from + slideSize, to + slideSize, slideSize, tail)
            .observeOn(Schedulers.io()));

    return nonEmptyWindow.toFlowable().concatWith(nextWindow);
  }

  static class Data {
    private final Integer data;
    private final Double time;

    Data(Integer data, Double time) {
      this.data = data;
      this.time = time;
    }

    @Override
    public String toString() {
      return "Data{" +
          "data=" + data +
          ", time=" + time +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Data data1 = (Data) o;
      return Objects.equals(data, data1.data) &&
          Objects.equals(time, data1.time);
    }

    @Override
    public int hashCode() {
      return Objects.hash(data, time);
    }
  }

}
