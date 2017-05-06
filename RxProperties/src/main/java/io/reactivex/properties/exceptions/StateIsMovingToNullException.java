package io.reactivex.properties.exceptions;


import io.reactivex.properties.SwitchableState;

/**
 * a {@link RuntimeException} that is thrown when invoking {@link SwitchableState#next()} or
 * {@link SwitchableState#back()} and the result will be {@code null}
 * <p>
 * Created by Ahmed Adel on 12/31/2016.
 */
public class StateIsMovingToNullException extends RuntimeException {
}
