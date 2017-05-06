package com.base.abstraction.concurrency;

import android.support.annotation.CallSuper;

import com.base.abstraction.interfaces.Clearable;

/**
 * a parent class for any chain-able Object that will have an owner, which it will need to
 * return it's owner at some point in the flow
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
class OwnedReference implements Clearable {

    private Object owner;

    public OwnedReference owner(Object owner) {
        this.owner = owner;
        return this;
    }

    /**
     * chain the calls and return the Owner reference to be used back, this will return
     * {@code null} if {@link #owner(Object)} was not called at some point before this method
     *
     * @param <O> the expected return type
     * @return the Object that owns this reference
     * @throws ClassCastException if the expected type does not match the Object set through
     *                            {@link #owner(Object)}
     */
    @SuppressWarnings("unchecked")
    public <O> O chain() throws ClassCastException {
        return (O) owner;
    }

    @CallSuper
    @Override
    public void clear() {
        owner = null;
    }
}
