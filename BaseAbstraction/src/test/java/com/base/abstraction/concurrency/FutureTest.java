package com.base.abstraction.concurrency;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by Ahmed Adel on 2/26/2017.
 */
public class FutureTest {

    public FutureTest() {
    }


    @Test
    public void rxSingle() throws Exception {

        // arrange
        int valueOne = 1;
        int valueTwo = 1;

        //act
        Single<Integer> successfulSingle = sumAsync(valueOne, valueTwo);

        //assert
        successfulSingle.subscribe(assertSuccessful(valueOne, valueTwo), assertExceptionThrown());


    }

    private Single<Integer> sumAsync(final int valueOne, final int valueTwo) {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> e) throws Exception {
                Thread.sleep(200);
                if (valueOne < 0 || valueTwo < 0) {
                    e.onError(new IllegalArgumentException("negative values not allowed"));
                } else {
                    e.onSuccess(valueOne + valueTwo);
                }
            }
        });
    }


    @NonNull
    private Consumer<Throwable> assertExceptionThrown() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Assert.assertTrue(throwable instanceof IllegalArgumentException);
            }
        };
    }

    @NonNull
    private Consumer<Integer> assertSuccessful(final int valueOne, final int valueTwo) {
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Assert.assertTrue(integer == (valueOne + valueTwo));
            }
        };
    }


    @Test
    public void rxReduceOperator() throws Exception {

        // arrange
        Integer[] values = {1, 2, 3, 4, 5};

        // act
        Maybe<Integer> result = Observable
                .fromIterable(Arrays.asList(values))
                .reduce(sumValues());

        // assert
        Assert.assertTrue(result.blockingGet() == (1 + 2 + 3 + 4 + 5));
    }

    @NonNull
    private BiFunction<Integer, Integer, Integer> sumValues() {
        return new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integerOne, Integer integerTwo) throws Exception {
                return integerOne + integerTwo;
            }
        };
    }

}