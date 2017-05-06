package io.reactivex.properties.exceptions;


import io.reactivex.properties.SwitchableState;

/**
 * a {@link RuntimeException} that is thrown when a {@link SwitchableState} is trying to invoke
 * {@link SwitchableState#next()} or {@link SwitchableState#back()} and the data required to process
 * this method is determined as invalid
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public class StateNotValidToMoveException extends RuntimeException {
}
