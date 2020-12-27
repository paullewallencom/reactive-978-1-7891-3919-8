package com.tomekl007.rxjava.chapter_5;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class WindowOperatorTest {

  @Test
  public void shouldTestWindow() {
    Observable.range(1, 10)

        // Create windows containing at most 2 items, and skip 3 items before starting a new window.
        .window(2, 3)
        .flatMapSingle(window -> window.map(String::valueOf)
            .reduce(new StringJoiner(", ", "[", "]"), StringJoiner::add))
        .subscribe(System.out::println);
  }

  @Test
  public void shouldWindowByTime() {
    // given
    final TestScheduler scheduler = new TestScheduler();

    Observable.range(1, 10)
        .window(5, TimeUnit.SECONDS, scheduler)
        .flatMapSingle(window -> window.map(String::valueOf)
            .reduce(new StringJoiner(", ", "[", "]"), StringJoiner::add))
        .subscribe(System.out::println);
    // when advance by time
    scheduler.advanceTimeBy(10, TimeUnit.SECONDS);

    //adding new events to observable will fill next window
  }



  static class Activity {
    private final String userId;
    private final Boolean clicked;

    Activity(String userId, Boolean clicked) {
      this.userId = userId;
      this.clicked = clicked;
    }

    public static Activity newInstance(String u1, boolean b) {
      return new Activity(u1, b);
    }

    public String getUserId() {
      return userId;
    }

    public Boolean getClicked() {
      return clicked;
    }

    @Override
    public String toString() {
      return "Activity{" +
          "userId='" + userId + '\'' +
          ", clicked=" + clicked +
          '}';
    }
  }
}
