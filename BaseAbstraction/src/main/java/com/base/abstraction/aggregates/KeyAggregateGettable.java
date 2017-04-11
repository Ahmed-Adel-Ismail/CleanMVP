package com.base.abstraction.aggregates;

/**
 * interface implemented by classes that has an Aggregate data and want an API to check if it
 * contains a certain Object or not, similar to {@link java.util.Map#get(Object)}
 * <p>
 * Created by Ahmed Adel on 10/26/2016.
 *
 * @param <Parameter> the type of the key to search for
 * @param <Return>    the type of the returned value
 */
public interface KeyAggregateGettable<Parameter, Return> extends KeyAggregate {

    /**
     * get an object from the current Aggregate mapped to the given key
     *
     * @param key the key to search for
     * @return the value if found, or {@code null} if not found
     */
    Return get(Parameter key);

}
