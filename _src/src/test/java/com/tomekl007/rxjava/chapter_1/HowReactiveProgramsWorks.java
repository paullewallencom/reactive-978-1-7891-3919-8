package com.tomekl007.rxjava.chapter_1;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HowReactiveProgramsWorks {
  private String result = "";

  @Test
  public void givenArray_whenMapAndSubscribe_thenReturnCapitalLetters() {
    String[] letters = {"a", "b", "c", "d", "e", "f", "g"};

    Observable.fromArray(letters)
        .map(String::toUpperCase)
        .subscribe(letter -> result += letter);

    assertEquals("ABCDEFG", result);
  }

  @Test
  public void givenArray_whenFlatMapAndSubscribe_thenReturnUpperAndLowerCaseLetters() {

    Observable.just("book1", "book2")
        .flatMap(s -> getTitle())
        .subscribe(l -> result += l);

    assertEquals("titletitle", result);
  }

  private static String[] titles = {"title"};
  private static List<String> titleList = Arrays.asList(titles);

  static Observable<String> getTitle() {
    return Observable.fromIterable(titleList);
  }


  @Test
  public void givenArray_whenScanAndSubscribe_thenReturnTheSumOfAllLetters() {
    String[] letters = {"a", "b", "c"};

    Observable.fromArray(letters)
        .scan(new StringBuilder(), StringBuilder::append)
        .subscribe(total -> result += total.toString());

    assertEquals("aababc", result);
  }

  @Test
  public void givenArrayOfNumbers_whenGroupBy_thenCreateTwoGroupsBasedOnParity() {
    Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    String[] EVEN = {""};
    String[] ODD = {""};

    Observable.fromArray(numbers)
        .groupBy(i -> 0 == (i % 2) ? "EVEN" : "ODD")
        .subscribe(group ->
            group.subscribe((number) -> {
              if (group.getKey().equals("EVEN")) {
                EVEN[0] += number;
              } else {
                ODD[0] += number;
              }
            })
        );

    assertEquals("0246810", EVEN[0]);
    assertEquals("13579", ODD[0]);
  }
}
