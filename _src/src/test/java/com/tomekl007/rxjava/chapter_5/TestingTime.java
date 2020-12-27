package com.tomekl007.rxjava.chapter_5;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class TestingTime {

  @Test
  public void givenDelay_whenAdvanceInTimeTooLow_thenWillNotFinishProcessing() {
    //given
    List<String> actualOutput = new ArrayList<>();
    final TestScheduler scheduler = new TestScheduler();
    final List<String> keywordToSearch = Arrays.asList("u", "us", "usi", "usin", "using");

    //when
    Observable.fromIterable(keywordToSearch)
        .flatMap(s -> Observable
            .just(s + " FirstResult", s + " SecondResult")
            .delay(10, TimeUnit.SECONDS, scheduler))
        .toList()
        .doOnSuccess(actualOutput::addAll)
        .subscribe();

    scheduler.advanceTimeBy(5, TimeUnit.SECONDS);

    //then
    assertThat(actualOutput).containsExactly();
  }

  @Test
  public void givenDelay_whenAdvanceInTimeEnough_thenWillFinishProcessing() {
    //given
    List<String> actualOutput = new ArrayList<>();
    final TestScheduler scheduler = new TestScheduler();
    final List<String> keywordToSearch = Arrays.asList("u", "us", "usi", "usin", "using");

    //when
    Observable.fromIterable(keywordToSearch)
        .flatMap(s -> Observable
            .just(s + " FirstResult", s + " SecondResult")
            .delay(10, TimeUnit.SECONDS, scheduler))
        .toList()
        .doOnSuccess(actualOutput::addAll)
        .subscribe();

    scheduler.advanceTimeBy(20, TimeUnit.SECONDS);

    //then
    assertThat(actualOutput).containsExactly("u FirstResult", "u SecondResult",
        "us FirstResult", "us SecondResult",
        "usi FirstResult", "usi SecondResult",
        "usin FirstResult", "usin SecondResult",
        "using FirstResult", "using SecondResult");
  }

  @Test
  public void givenObservable_whenSwitchmap_shouldAssertLatestItemReturned() {
    //given
    List<String> actualOutput = new ArrayList<>();
    final TestScheduler scheduler = new TestScheduler();
    final List<String> keywordToSearch = Arrays.asList("u", "us", "usi", "usin", "using");

    //when
    Observable.fromIterable(keywordToSearch)
        .switchMap(s -> Observable //switchMap result in different logic
            .just(s + " FirstResult", s + " SecondResult")
            .delay(10, TimeUnit.SECONDS, scheduler))
        .toList()
        .doOnSuccess(actualOutput::addAll)
        .subscribe();

    scheduler.advanceTimeBy(1, TimeUnit.MINUTES);

    //then
    assertEquals(2, actualOutput.size());
    assertThat(actualOutput).containsExactly("using FirstResult", "using SecondResult");
  }
}
