package com.base.abstraction.exceptions.aggregates;

/**
 * a {@link RuntimeException} that indicates that the index is invalid
 * <p>
 * Created by Ahmed Adel Ismail on 4/24/2017.
 */
public class InvalidIndexException extends RuntimeException {

    public InvalidIndexException() {
    }

    public InvalidIndexException(String detailMessage) {
        super(detailMessage);
    }
}
