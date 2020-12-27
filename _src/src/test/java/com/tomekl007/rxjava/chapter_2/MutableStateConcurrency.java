package com.tomekl007.rxjava.chapter_2;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class MutableStateConcurrency {

  @Test
  public void two_threads_on_access_to_mutable_map() {
    //given
    Map<String, String> mutableMap = Collections.synchronizedMap(new HashMap<>());
    mutableMap.put("USA", "123");
    mutableMap.put("Poland", "321");

    Thread thread = new Thread(() ->
        mutableMap.put("a", "b")
        //modification is possible so every access to mutableMap must be synchronized
    );

    Thread thread2 = new Thread(() ->
        mutableMap.put("a", "b")
    );


  }
}
