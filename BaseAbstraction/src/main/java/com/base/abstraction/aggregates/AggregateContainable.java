package com.base.abstraction.aggregates;

/**
 * interface implemented by classes that has an Aggregate data and want an API to check if it
 * contains a certain Object or not, similar to {@link java.util.Collection#contains(Object)}
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
public interface AggregateContainable<Type> extends Aggregate {

    /**
     * check if the current instance contains the passed Object or not, similar to
     * {@link java.util.Collection#contains(Object)}
     *
     * @param object the Object to search for
     * @return {@code true} if it exists, else {@code false}
     */
    boolean contains(Type object);

}
