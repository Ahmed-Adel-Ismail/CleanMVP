package io.reactivex.properties.exceptions;

import io.reactivex.properties.Property;

/**
 * a {@link RuntimeException} that encapsulates any {@link Exception} and throw it as a
 * {@link RuntimeException}, this is mainly used in {@link Property} sub-classes
 * <p>
 * Created by Ahmed Adel Ismail on 5/6/2017.
 */
public class InvocationException extends RuntimeException {

    public InvocationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
