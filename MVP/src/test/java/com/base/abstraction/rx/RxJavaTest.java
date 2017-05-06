package com.base.abstraction.rx;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * Created by ESC on 2/5/2017.
 */

public class RxJavaTest {
    public RxJavaTest() {
    }


    public void zipOperator() throws Exception {


        System.out.println("waiting for 2 observables to emit there items then print them");
        System.out.println("==============");

        List<Integer> indexes = Arrays.asList(0, 1, 2, 3, 4);
        List<String> letters = Arrays.asList("a", "b", "c", "d", "e");

        Observable<Integer> indexesObs = Observable.fromIterable(indexes);

        Observable<String> lettersObs = Observable.fromIterable(letters);

        Observable<String> zip1 = Observable.zip(indexesObs, lettersObs,
                mergeTwoObservablesEmittedItems());

        zip1.subscribe(printMergedItems());


        System.out.println("\n");
        System.out.println("waiting for 3 observable to emit there items then print them");
        System.out.println("==============");

        // declare a third observable to use which will cause a delay of 1 second for each item
        Observable<Integer> delayedObs = Observable.create(delayedEmitter());

        Observable<String> zip2 = Observable.zip(indexesObs, lettersObs, delayedObs,
                mergeThreeObservablesEmittedItems());

        zip2.subscribe(printMergedItems());


        System.out.println("\n");
        System.out.println("combining 2 zip() operators");
        System.out.println("==============");

        Observable<String> zip3 = Observable.zip(zip1, zip2, mergeTwoZippedResults());
        zip3.subscribe(printMergedItems());

    }


    @NonNull
    private BiFunction<Integer, String, String> mergeTwoObservablesEmittedItems() {
        return new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer index, String letter) throws Exception {
                return "[" + index + "] " + letter;
            }
        };
    }

    @NonNull
    private ObservableOnSubscribe<Integer> delayedEmitter() {
        return new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                System.out.println("delayed emitter thread : " + Thread.currentThread().getId());
                for (int delayStep = 0; delayStep < 5; delayStep++) {
                    Thread.sleep(1000);
                    e.onNext(delayStep);
                }
                e.onComplete();
            }
        };
    }


    @NonNull
    private Function3<Integer, String, Integer, String> mergeThreeObservablesEmittedItems() {
        return new Function3<Integer, String, Integer, String>() {
            @Override
            public String apply(Integer index, String letter, Integer delayStep) throws Exception {
                return "[" + index + "] " + letter + " @ " + (delayStep * 1000) + " milliseconds";
            }
        };
    }

    @NonNull
    private BiFunction<String, String, String> mergeTwoZippedResults() {
        return new BiFunction<String, String, String>() {
            @Override
            public String apply(String zip1Result, String zip2Result) throws Exception {
                return "{zip1=" + zip1Result + ", zip2=" + zip2Result + "}";
            }
        };
    }


    @NonNull
    private Consumer<String> printMergedItems() {
        return new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        };
    }



}
