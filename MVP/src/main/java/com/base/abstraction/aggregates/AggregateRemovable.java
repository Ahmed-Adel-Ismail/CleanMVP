package com.base.abstraction.aggregates;

/**
 * an interface for {@link Aggregate} alike implementers that have a {@code remove()} mechanism,
 * notice that implementing such interface will make your implementer {@code Mutable}
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 *
 * @param <Return>    the expected return type
 * @param <Parameter> the type of the Object to be removed
 */
public interface AggregateRemovable<Return, Parameter> extends KeyAggregate {

    /**
     * remove an Object from the current Aggregate
     *
     * @param p the Object to remove
     * @return an Object indicating the result of the operation
     */
    Return remove(Parameter p);

}
