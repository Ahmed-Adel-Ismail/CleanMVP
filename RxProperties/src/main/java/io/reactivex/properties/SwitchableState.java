package io.reactivex.properties;


import io.reactivex.properties.exceptions.StateIsMovingToNullException;
import io.reactivex.properties.exceptions.StateNotValidToMoveException;

/**
 * an interface that is implemented by Objects that can switch between States in a next/back
 * manner
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public interface SwitchableState<State extends SwitchableState> {

    /**
     * move to the next {@link SwitchableState}
     *
     * @return the next {@link SwitchableState}
     * @throws StateIsMovingToNullException if the next {@link SwitchableState} is {@code null}
     * @throws StateNotValidToMoveException if the current {@link SwitchableState} is not considered
     *                                      valid so it can move to the next one
     */
    State next() throws StateIsMovingToNullException, StateNotValidToMoveException;

    /**
     * move to the previous {@link SwitchableState}
     *
     * @return the previous {@link SwitchableState}
     * @throws StateIsMovingToNullException if the previous {@link SwitchableState} is {@code null}
     * @throws StateNotValidToMoveException if the current {@link SwitchableState} is not considered
     *                                      valid so it can move to the previous one
     */
    State back() throws StateIsMovingToNullException, StateNotValidToMoveException;


}
