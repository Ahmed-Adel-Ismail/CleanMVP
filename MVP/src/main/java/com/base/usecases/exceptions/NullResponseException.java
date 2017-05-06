package com.base.usecases.exceptions;

import com.base.usecases.requesters.base.EntityRequester;

/**
 * a {@link RuntimeException} that indicates a {@code null} response from the
 * {@link EntityRequester} implementer
 * <p>
 * Created by Ahmed Adel Ismail on 4/26/2017.
 */
public class NullResponseException extends RuntimeException {

    public NullResponseException() {
    }

    public NullResponseException(String detailMessage) {
        super(detailMessage);
    }
}
