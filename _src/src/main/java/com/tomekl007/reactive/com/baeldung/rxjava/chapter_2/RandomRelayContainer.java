package com.tomekl007.reactive.com.baeldung.rxjava.chapter_2;

import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRelayContainer extends Relay<Integer> {
    private Random random = new Random();

    private List<Observer<? super Integer>> observers = new ArrayList<>();

    @Override
    public void accept(Integer integer) {
        int observerIndex = random.nextInt(observers.size()) & Integer.MAX_VALUE;
        observers.get(observerIndex).onNext(integer);
    }

    @Override
    public boolean hasObservers() {
        return observers.isEmpty();
    }

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        observers.add(observer);
        observer.onSubscribe(Disposables.fromRunnable(() -> System.out.println("Disposed")));
    }
}
