package io.reactivex.properties;

import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.junit.Assert.assertTrue;

/**
 * Created by Ahmed Adel Ismail on 5/6/2017.
 */
public class PropertyTest {


    @Test
    public void iterateOverEmptyList() throws Exception {
        final BooleanProperty result = new BooleanProperty(true);
        Observable.fromIterable(new ArrayList<>()).forEach(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                result.set(false);
            }
        });
        assertTrue(result.isTrue());
    }

    @Test
    public void asObservableWithInitialValue() throws Exception {
        final Property<Integer> result = new Property<>(0);
        Property<Integer> property = new Property<>(10);
        property.asObservable().subscribe(result);
        assertTrue(result.get().equals(10));
    }

    @Test
    public void asObservableWithLazySetValue() throws Exception {
        final Property<Integer> result = new Property<>(0);
        Property<Integer> property = new Property<>();
        property.asObservable().subscribe(result);
        property.set(20);
        assertTrue(result.get().equals(20));
    }

    @Test
    public void asObservableThenDisposeThenSetValueAgain() throws Exception {
        final Property<Integer> result = new Property<>(0);
        result.onAccept(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                System.out.println(integer);
                result.set(result.get() + integer);
                return integer;
            }
        });


        Property<Integer> property = new Property<>();
        Disposable d = property.asObservable().subscribe(result);


        property.set(20);
        d.dispose();
        property.set(30);


        assertTrue(result.get() == 20);
    }

    @Test
    public void asObservableForTwoSubscribersAndBothAreUpdated() throws Exception {
        final Property<Integer> resultOne = new Property<>(0);
        final Property<Integer> resultTwo = new Property<>(0);

        Property<Integer> property = new Property<>();
        property.asObservable().subscribe(resultOne);
        property.asObservable().subscribe(resultTwo);

        property.set(20);

        assertTrue(resultOne.get().equals(resultTwo.get()) && resultOne.get().equals(20));
    }

    @Test
    public void asIterableObservable() throws Exception {
        final BooleanProperty result = new BooleanProperty(false);
        Property<ArrayList<String>> property = new Property<>(new ArrayList<String>());
        property.get().add("A");
        property.get().add("B");
        property.get().add("C");
        property.get().add("D");

        property.asObservableFromIterable(String.class).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
                result.set(true);
            }
        });
        assertTrue(result.isTrue());
    }


}