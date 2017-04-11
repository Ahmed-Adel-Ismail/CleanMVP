package com.base.presentation.interfaces;

/**
 * same as {@link Destroyable} but has methods to let the outside classes check for this instance
 * weather it is destroyed or not
 * <p/>
 * Created by Ahmed Adel on 9/1/2016.
 */
public interface DestroyableClient extends Destroyable {


    /**
     * check if the initial Object is considered as Destroyed or not
     *
     * @return {@code true} if this Object is not eligible for usage any more
     */
    boolean isDestroyed();

}
