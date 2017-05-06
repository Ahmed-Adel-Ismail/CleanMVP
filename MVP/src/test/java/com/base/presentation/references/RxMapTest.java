package com.base.presentation.references;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by Ahmed Adel on 2/21/2017.
 */

public class RxMapTest {


    @Test
    public void rxJavaMapOperator() throws Exception {

        List<Property<Integer>> intProperties = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            intProperties.add(new Property<>(i));
        }


        List<Integer> result = Observable
                .fromIterable(intProperties)
                .map(new Function<Property<Integer>, Integer>() {
                    @Override
                    public Integer apply(Property<Integer> integerProperty) throws Exception {
                        return integerProperty.get();
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer != 5;
                    }
                })
                .toList()
                .blockingGet();

        Assert.assertTrue(result.size() == 9);
    }

}
