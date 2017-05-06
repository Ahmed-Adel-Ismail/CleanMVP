package com.base.presentation.references;

import com.base.abstraction.concurrency.Future;

import java.util.concurrent.Callable;

/**
 * a class that represents a Function call in {@link Weaver}
 * <p>
 * Created by Ahmed Adel on 2/15/2017.
 */
public class WeaverMethod<T , F extends Future<?>> implements Callable<F> {

    private final long id;
    private final Future<?> future;
    private Weaver<T> weaver;
    private Object parameterOne;
    private Object parameterTwo;
    private Object parameterThree;

    WeaverMethod(Weaver<T> weaver, long id, Future<?> future) {
        this.weaver = weaver;
        this.id = id;
        this.future = future;
    }

    public WeaverMethod<T, F> parameterOne(Object parameterOne) {
        this.parameterOne = parameterOne;
        return this;
    }

    public WeaverMethod<T, F> parameterTwo(Object parameterTwo) {
        this.parameterTwo = parameterTwo;
        return this;
    }

    public WeaverMethod<T, F> parameterThree(Object parameterThree) {
        this.parameterThree = parameterThree;
        return this;
    }


    @Override
    public F call() throws UnsupportedOperationException {
        if (weaver.methods.contains(id)) {
            return invokeRun();
        } else {
            throw new UnsupportedOperationException("no functions available for this id");
        }
    }

    @SuppressWarnings("unchecked")
    private F invokeRun() {
        weaver.methods.execute(id, new WeaverTuple.Builder<T>()
                .weaver(weaver)
                .parameterOne(parameterOne)
                .parameterTwo(parameterTwo)
                .parameterThree(parameterThree)
                .future(future)
                .build());
        return (F) future;
    }

}
