package com.tomekl007.rxjava.chapter_5;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class AddingConceptOfTime {

  @Test
  public void givenObservable_whenDelay_thenIntroduceConceptOfTimeInStreams() throws InterruptedException {
    //given
    List<String> actualOutput = new ArrayList<>();
    final List<String> keywordToSearch = Arrays.asList("u", "us", "usi", "usin", "using");

    //when
    Observable.fromIterable(keywordToSearch)
        .flatMap(s -> Observable
            .just(s + " FirstResult", s + " SecondResult")
            .delay(10, TimeUnit.SECONDS))
        .toList()
        .doOnSuccess(actualOutput::addAll)
        .subscribe();

    //how to delay check?
    Thread.sleep(11_000); //BAD! our test will take at least 11 seconds!

    //then
    assertThat(actualOutput).contains("u FirstResult", "u SecondResult",
        "us FirstResult", "us SecondResult",
        "usi FirstResult", "usi SecondResult",
        "usin FirstResult", "usin SecondResult",
        "using FirstResult", "using SecondResult");
  }

}
