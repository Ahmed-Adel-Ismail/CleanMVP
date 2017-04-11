package com.example.basepresentation;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Ahmed Adel on 3/15/2017.
 */

public class RxJavaTest2 {


    @Test
    public void disposeFromIterable() throws Exception {

        Disposable disposable = Observable.fromArray(1, 2, 3, 4, 5).subscribe();
        Assert.assertTrue(disposable.isDisposed());

    }


    @Test
    public void disposeFromCallable() throws Exception {
        Disposable disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // do nothing
                return new Object();
            }
        }).subscribe();
        Assert.assertTrue(disposable.isDisposed());

    }

    @Test
    public void disposeFromSubject() throws Exception {

        PublishSubject<Integer> subject = PublishSubject.create();
        Disposable disposable = subject.subscribe();

        subject.onNext(1);
        subject.onNext(2);
        subject.onComplete();

        Assert.assertTrue(disposable.isDisposed());

    }

    @Test
    public void mergeOperator() throws Exception {

        final List<Integer> intsToReduce = new ArrayList<>();
        final PublishSubject<Integer> loadInts = PublishSubject.create();
        loadInts.subscribe(reduceOnNextObserver(intsToReduce));

        final PublishSubject<Double> loadDoubles = PublishSubject.create();
        Observable.mergeDelayError(loadInts, loadDoubles)
                .onErrorResumeNext(negativeNumbersObservable())
                .onErrorReturn(negativeOne())
                .subscribe(printOnNextObserver(), printError());


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    loadInts.onNext(i);
                    try {

                        Thread.sleep(1000);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                loadInts.onError(new ArithmeticException());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (double i = 0.1; i < 1; i = i + 0.1) {
                    loadDoubles.onNext(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                loadDoubles.onComplete();
            }
        }).start();

        Thread.sleep(22 * 1000);

    }


    private Function<Throwable, Integer> negativeOne() {
        return new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                return -100000;
            }
        };
    }

    private Function<Throwable, ObservableSource<Integer>> negativeNumbersObservable() {
        return new Function<Throwable, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Throwable throwable) throws Exception {
                return emitNegativeNumbers();
            }
        };
    }


    private ObservableSource<Integer> emitNegativeNumbers() {
        return new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                for (int i = -1; i > -10; i--) {
                    observer.onNext(i);
                }
            }
        };
    }

    private Consumer<Throwable> printError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError()");
            }
        };
    }

    private Consumer<Integer> reduceOnNextObserver(final List<Integer> intsToReduce) {
        return new Consumer<Integer>() {

            @Override
            public void accept(Integer integer) throws Exception {
                intsToReduce.add(integer);
                int sum = Observable.fromIterable(intsToReduce).reduce(sumAll()).blockingGet();
                System.out.println("sum : " + sum);
            }

            private BiFunction<Integer, Integer, Integer> sumAll() {
                return new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer firstInteger, Integer secondInteger) throws Exception {
                        return firstInteger + secondInteger;
                    }
                };
            }
        };
    }


    private Consumer<Number> printOnNextObserver() {
        return new Consumer<Number>() {
            @Override
            public void accept(Number object) throws Exception {
                System.out.println("onNext() : " + object);
            }
        };
    }


}
