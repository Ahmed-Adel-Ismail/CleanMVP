package com.base.entities.interfaces;

/**
 * this interface is intended for {@code enums} (or similar classes that needs
 * to be identified by a value) who are based on a value for each constant,
 * where it has the utility methods to identify the type ({@code enum} constant for example)
 * based on the value passed, and can declare it's value in the interface as
 * well
 *
 * @param <T> the type of the constants (you can pass the {@code enum} as a type if you expect it as a result)
 * @param <V> the type of the value which identifies this constant
 *            <p/>
 *            Created by Ahmed Adel on 8/31/2016.
 */
public interface TypedValuable<T, V> {

    /**
     * get the Type based on the passed value
     *
     * @param value the value that identifies the Type
     * @return the Type
     */
    T getType(V value);

    /**
     * get the value that identifies this type
     *
     * @return a value that can be used later in {@link #getType(Object)}
     */
    V getValue();
}