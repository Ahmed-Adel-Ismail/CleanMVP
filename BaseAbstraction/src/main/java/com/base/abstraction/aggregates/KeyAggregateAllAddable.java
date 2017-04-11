package com.base.abstraction.aggregates;

/**
 * an interface for {@link Aggregate} alike implementers that have a {@code addAll(key,value)}
 * mechanism,notice that implementing such interface will make your implementer {@code Mutable}
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 *
 * @param <Return>    the return type of {@link #putAll(KeyAggregate)}
 * @param <Parameter> the type to be put
 */
public interface KeyAggregateAllAddable<Return, Parameter extends KeyAggregate>
        extends KeyAggregate {

    /**
     * put All values in the current instance
     *
     * @param p the Object that holds values to be added
     * @return an Object indicating the result of the operation
     */
    Return putAll(Parameter p);
}
