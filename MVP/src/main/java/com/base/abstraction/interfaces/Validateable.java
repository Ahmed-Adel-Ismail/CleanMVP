package com.base.abstraction.interfaces;

/**
 * implement this interface in Objects that will have a certain logic to determine weather this
 * Object is Valid or not
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 */
public interface Validateable {

    /**
     * check if the current {@link Validateable} Class is considered Valid or not
     *
     * @return {@code true} if it is valid, else {@code false}
     */
    boolean isValid();
}
