package com.base.abstraction.interfaces;

/**
 * implement this interface if your class will need to call the sub-classes {@link #clear()}
 * method inside it's {@link #clear()} method as well, in this case, it will need to mark
 * the {@link #clear()} method as {@code final}, and let the sub-class override the
 * {@link #onClear()} method, and call it inside it's own {@link #clear()}
 * <p>
 * in other words, the {@link #onClear()} is a {@code Template} method to be Overriden
 * by sub-classes
 * <p>
 * Created by Ahmed Adel on 9/19/2016.
 */
public interface ClearableParent extends Clearable {

    /**
     * implement the {@link #clear()} for sub-classes in this method ... this method is
     * supposed to be used by sub-class only, it is <b>NOT</b> part of the interface
     *
     * @see ClearableParent
     */
    void onClear();
}
