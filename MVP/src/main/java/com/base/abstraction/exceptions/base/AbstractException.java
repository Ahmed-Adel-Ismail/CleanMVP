package com.base.abstraction.exceptions.base;

/**
 * a {@link RuntimeException} that holds common behaviors between exceptions in the
 * application
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractException extends RuntimeException {

    public AbstractException() {
    }

    public AbstractException(String detailMessage) {
        super(detailMessage);
    }

    public AbstractException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AbstractException(Throwable throwable) {
        super(throwable);
    }
}
