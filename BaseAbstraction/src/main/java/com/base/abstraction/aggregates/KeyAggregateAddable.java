package com.base.abstraction.aggregates;

/**
 * an interface for {@link Aggregate} alike implementers that have a {@code add(key,value)}
 * mechanism,notice that implementing such interface will make your implementer {@code Mutable}
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 *
 * @param <ParameterKey>   the type of the key for {@link #put(Object, Object)}
 * @param <ParameterValue> the type of the value for {@link #put(Object, Object)}
 */

public interface KeyAggregateAddable<ParameterKey, ParameterValue> extends KeyAggregate {

    /**
     * add an Object to the current Aggregate mapped to the given key
     *
     * @param key   the key to find this Object
     * @param value the Object to be added
     * @return an Object indicating the result of the operation
     */
    ParameterValue put(ParameterKey key, ParameterValue value);

}
