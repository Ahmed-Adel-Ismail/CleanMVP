package com.base.abstraction.state;

/**
 * implement this interface if your class will use the State pattern to switch between
 * several states in a Iterable-like manner
 * <p>
 * the best implementation for this interface is to make each state execute it's code in it's
 * {@code constructor}, so when a new instance of this State is created when {@link #next()}
 * is invoked, it's code is executed
 * <p>
 * Created by Ahmed Adel on 9/27/2016.
 *
 * @param <State> the implementer of this interface
 */
public interface IteratorState<State extends IteratorState<State>> {

    /**
     * check if this {@link FlowControllable} has next State or not
     *
     * @return {@code true} if you can call {@link #next()}, else {@code false}
     */
    boolean hasNext();

    /**
     * move to next {@link FlowControllable}, make sure that {@link #hasNext()} returns {@code true}
     * before invoking this method
     *
     * @return the next {@link FlowControllable}
     */
    State next();

}
