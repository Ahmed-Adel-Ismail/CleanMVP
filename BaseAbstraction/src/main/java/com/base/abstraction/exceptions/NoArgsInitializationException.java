package com.base.abstraction.exceptions;

import com.base.abstraction.exceptions.base.AbstractException;
import com.base.abstraction.logs.Logger;

/**
 * an {@link Exception} that is thrown when you try to initialize an instance from
 * {@link Class#newInstance()} while it has no no-arguments constructor
 * <p>
 * Created by Ahmed Adel on 9/21/2016.
 */
public class NoArgsInitializationException extends AbstractException {

    public NoArgsInitializationException(Class<?> thrower, Throwable cause) {
        super("failed to initialize an instance throw " + thrower + ".newInstance()" +
                " : make sure you provide a no-arguments constructor");
        super.initCause(cause);
        Logger.getInstance().exception(this);
    }
}
