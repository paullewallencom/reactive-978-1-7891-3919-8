package com.tomekl007.reactive.com.baeldung.rxjava.chapter_1;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class SubjectsProvider {

    public static Integer subscriber1 = 0;
    public static Integer subscriber2 = 0;

    private static Integer subjectMethod() {
        PublishSubject<Integer> subject = PublishSubject.create();

        subject.subscribe(firstObserver());

        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);

        subject.subscribe(secondObserver());

        subject.onNext(4);
        subject.onComplete();
        return subscriber1 + subscriber2;
    }


    public static Observer<Integer> firstObserver() {
        return new Observer<>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                subscriber1 += value;
                System.out.println("Subscriber1: " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("Subscriber1 completed");
            }

        };
    }

    public static Observer<Integer> secondObserver() {
        return new Observer<>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                subscriber2 += value;
                System.out.println("Subscriber2: " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("Subscriber2 completed");
            }

        };
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(subjectMethod());
    }
}
