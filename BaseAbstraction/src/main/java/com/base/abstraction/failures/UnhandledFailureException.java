package com.base.abstraction.failures;

import com.base.abstraction.actors.registries.ActorSystem;
import com.base.abstraction.exceptions.failures.Failure;
import com.base.abstraction.logs.Logger;

/**
 * if a {@link Failure} is thrown and not handled, this exception will be thrown, which
 * means that there is a missing {@link FailureHandler} in the application
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 *
 * @see Failure
 * @see FailureHandler
 * @see ActorSystem
 */
public class UnhandledFailureException extends RuntimeException {

    {
        Logger.getInstance().exception(this);
    }

    public UnhandledFailureException() {
    }

    public UnhandledFailureException(String detailMessage) {
        super(detailMessage);
    }

    public UnhandledFailureException(Throwable throwable) {
        this(throwable.getMessage());
        setStackTrace(throwable.getStackTrace());
    }
}
