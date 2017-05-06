package com.base.abstraction.aggregates;

/**
 * an interface for {@link Aggregate} alike implementers that have a {@code add()} mechanism,
 * notice that implementing such interface will make your implementer {@code Mutable}
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 *
 * @param <Return>    the return type
 * @param <Parameter> the parameter type
 */
public interface AggregateAddable<Return, Parameter> extends Aggregate {

    /**
     * add an Object to the current Aggregate
     *
     * @param p the Object to add
     * @return an Object indicating the result of the operation
     */
    Return add(Parameter p);

}
