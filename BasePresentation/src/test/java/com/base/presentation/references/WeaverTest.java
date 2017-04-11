package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * Created by Ahmed Adel on 2/15/2017.
 */
public class WeaverTest {

    private static final long ID_HUMAN_AGE = 1;
    private static final long ID_HUMAN_COPY_NAME = 2;
    private static final long ID_HUMAN_COUNT_NAME_CHARS = 3;


    public WeaverTest() {
    }

    @Test
    public void set_20$$age_is_set() throws Exception {
        Weaver<Human> weaver = new Weaver<>(new Human());
        weaver.setVariable(ID_HUMAN_AGE, 20);
        int age = weaver.getVariable(ID_HUMAN_AGE);
        Assert.assertTrue(age == 20);
    }


    @Test
    public void setMethod_copyName$$nameCopied() throws Exception {
        final Property<String> nameCopy = new Property<>();
        Weaver<Human> weaver = new Weaver<>(new Human());
        weaver.setMethod(ID_HUMAN_COPY_NAME, copyName(nameCopy));
        weaver.method(ID_HUMAN_COPY_NAME).call();
        Assert.assertTrue(nameCopy.get().equals(weaver.get().name));


    }

    @NonNull
    private Command<WeaverTuple<Human>, Void> copyName(final Property<String> nameCopy) {
        return new Command<WeaverTuple<Human>, Void>() {
            @Override
            public Void execute(WeaverTuple<Human> p) {
                if (!p.getWeaver().isEmpty()) {
                    nameCopy.set(p.getWeaver().get().name);
                }
                return null;
            }
        };
    }


    @Test
    public void setMethod_countName$$future_onComplete_greater_than_zero_chars() throws Exception {

        final Property<Integer> nameCount = new Property<>(0);
        Weaver<Human> human = new Weaver<>(new Human());
        human.setMethod(ID_HUMAN_COUNT_NAME_CHARS, countNameChars());

        Future<Integer> future = new Future<>();
        human.method(ID_HUMAN_COUNT_NAME_CHARS, future).call();

        future.onComplete(countName(nameCount)).onException(errorValue(nameCount));
        Assert.assertTrue(nameCount.get() > 0);

    }

    @NonNull
    private Command<WeaverTuple<Human>, Void> countNameChars() {
        return new Command<WeaverTuple<Human>, Void>() {
            @Override
            public Void execute(WeaverTuple<Human> p) {
                Future<Integer> future = p.getFuture();
                if (!p.getWeaver().isEmpty()) {
                    future.setResult(p.getWeaver().get().name.length());
                } else {
                    future.setException(new NullPointerException("weaver cleared"));
                }
                return null;
            }
        };
    }

    @NonNull
    private Command<Integer, Void> countName(final Property<Integer> nameCount) {
        return new Command<Integer, Void>() {
            @Override
            public Void execute(Integer p) {
                nameCount.set(p);
                return null;
            }
        };
    }

    @NonNull
    private Command<Throwable, Void> errorValue(final Property<Integer> nameCount) {
        return new Command<Throwable, Void>() {
            @Override
            public Void execute(Throwable p) {
                nameCount.set(-1);
                return null;
            }
        };
    }


    private static class Human implements Serializable {
        String name = "El 7ag";
    }


}