package com.base.abstraction.state;

/**
 * implement this interface if your class will use the State pattern to switch between
 * several states in a ApplicationFlow-Control like behavior
 * <p/>
 * Created by Ahmed Adel on 9/27/2016.
 *
 * @param <State> the implementer of this interface
 *                <p/>
 * @see IteratorState
 */
public interface FlowControllable<State extends FlowControllable<State>>
        extends IteratorState<State> {


    /**
     * check if this {@link FlowControllable} has a previous State or not
     *
     * @return {@code true} if you can call {@link #back()}, else {@code false}
     */
    boolean hasBack();

    /**
     * move to the previous {@link FlowControllable}, make sure that {@link #hasBack()} returns
     * {@code true} before invoking this method
     *
     * @return @return the previous {@link FlowControllable}
     */
    State back();
}
