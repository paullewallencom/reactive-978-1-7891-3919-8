package com.tomekl007.rxjava.chapter_6;

import java.util.List;

public class ComputeFunction {
  public static void compute(List<Integer> integers) {
    System.out.println("process batch of elements:" + integers);
  }

  public static void compute(Integer v) {
    System.out.println("Expensive consumer operation for value v: " + v);
  }
}
