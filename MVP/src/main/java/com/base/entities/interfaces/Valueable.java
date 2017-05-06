package com.base.entities.interfaces;

/**
 * implement this interface if your class can be represented as a value
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 *
 * @param <T> the type of this value
 */
public interface Valueable<T> {

    /**
     * the value that represents this instance
     *
     * @return a value Object
     */
    T getValue();
}
