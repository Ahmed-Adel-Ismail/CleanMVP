package com.base.abstraction.interfaces;

/**
 * an interface implemented by Classes that needs to have equality comparison other than
 * {@link Object#equals(Object)}
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public interface Equalable {

    /**
     * same as {@link Object#equals(Object)}, but used for classes like
     * Activity or Fragment, or enum that have there
     * {@link Object#equals(Object)} method as final
     *
     * @param other the other object to compare with
     * @return {@code true} if both instances are supposed to be equal, if you
     * have no implementation just return the result of
     * {@link Object#equals(Object)}
     */
    boolean isEqual(Object other);
}
