package com.tomekl007.rxjava.chapter_3;

import io.reactivex.Observable;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TupleRxTest {

  @Test
  public void shouldUsePairAsTuple_whenDoingRxTransformations() {
    //given
    List<String> modifications = Arrays.asList("ADD", "REDO", "DIVIDE", "COPY");

    //when
    Pair<String, Integer> result = Observable
        .fromIterable(modifications)
        .flatMap(
            (data1) -> methodThatChangeData(data1).map(obj -> Pair.of(data1, obj))//carry original object
        ).blockingFirst();

    //then
    assertThat(result).isEqualTo(Pair.of( "ADD", 3));
  }

  private Observable<Integer> methodThatChangeData(String data1) {
    return Observable.fromArray(data1.length());
  }
}
