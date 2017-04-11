package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.concurrency.FutureTask;

/**
 * a Tuple passed to the {@link Command} that is executed as a method added to the Object in the
 * {@link Weaver} property
 * <p>
 * Created by Ahmed Adel on 2/15/2017.
 */
class WeaverTuple<E> {

    private final Weaver<E> weaver;
    private final Object parameterOne;
    private final Object parameterTwo;
    private final Object parameterThree;
    private Future<?> future;

    private WeaverTuple(Builder<E> builder) {
        future = builder.future;
        weaver = builder.weaver;
        parameterOne = builder.parameterOne;
        parameterTwo = builder.parameterTwo;
        parameterThree = builder.parameterThree;
    }


    /**
     * get the first parameter set to this {@link WeaverTuple}
     *
     * @param <V> the expected return type
     * @return the value casted to the return type
     * @throws ClassCastException if the value stored does not match the expected return type
     */
    @SuppressWarnings("unchecked")
    public <V> V getParameterOne() throws ClassCastException {
        return (V) parameterOne;
    }

    /**
     * get the second parameter set to this {@link WeaverTuple}
     *
     * @param <V> the expected return type
     * @return the value casted to the return type
     * @throws ClassCastException if the value stored does not match the expected return type
     */
    @SuppressWarnings("unchecked")
    public <V> V getParameterThree() throws ClassCastException {
        return (V) parameterThree;
    }

    /**
     * get the third parameter set to this {@link WeaverTuple}
     *
     * @param <V> the expected return type
     * @return the value casted to the return type
     * @throws ClassCastException if the value stored does not match the expected return type
     */
    @SuppressWarnings("unchecked")
    public <V> V getParameterTwo() throws ClassCastException {
        return (V) parameterTwo;
    }


    /**
     * get the {@link Weaver} property that created this {@link WeaverTuple}
     *
     * @return the {@link Weaver} property
     */
    @NonNull
    public Weaver<E> getWeaver() {
        return weaver;

    }

    /**
     * get the {@link Future} that was passed by the {@link Weaver} property, this
     * future task is created to notify the method invoker that the execution has finished,
     * if the method invoker did not want to be notified, this method will return {@code null}
     *
     * @return a {@link FutureTask}, or {@code null}
     */
    @SuppressWarnings("unchecked")
    public <F extends Future<?>> F getFuture() {
        return (F) future;
    }


    static final class Builder<E> {
        private Future<?> future;
        private Weaver<E> weaver;
        private Object parameterOne;
        private Object parameterTwo;
        private Object parameterThree;

        Builder() {
        }

        Builder<E> future(Future<?> future) {
            this.future = future;
            return this;
        }

        Builder<E> weaver(Weaver<E> weaver) {
            this.weaver = weaver;
            return this;
        }

        Builder<E> parameterOne(Object parameterOne) {
            this.parameterOne = parameterOne;
            return this;
        }

        Builder<E> parameterTwo(Object parameterTwo) {
            this.parameterTwo = parameterTwo;
            return this;
        }

        Builder<E> parameterThree(Object parameterThree) {
            this.parameterThree = parameterThree;
            return this;
        }

        WeaverTuple<E> build() {
            return new WeaverTuple<>(this);
        }
    }

}