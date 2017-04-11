package com.base.abstraction.observer;

import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.propagated.DuckableException;
import com.base.abstraction.exceptions.propagated.ThrowableGroup;
import com.base.abstraction.logs.Logger;

/**
 * implement this interface if your class can throw multiple {@link Throwable} instances in a
 * single loop while you want this loop to continue after each iteration
 * <p/>
 * Created by Ahmed Adel on 9/6/2016.
 */
interface MultipleExceptionable {

    /**
     * invoked when an {@link Exception} is thrown in any
     * {@link Observer#onUpdate(Event)} for any observer
     *
     * @param throwableGroup the {@link ThrowableGroup} that holds all the thrown
     *                       {@link Throwable} objects
     */
    void onMultipleExceptionsThrown(ThrowableGroup throwableGroup);


    /**
     * the default implementation for the {@link MultipleExceptionable} interface
     */
    class Implementer implements MultipleExceptionable {

        @Override
        public final void onMultipleExceptionsThrown(ThrowableGroup throwableGroup) {
            Logger.getInstance().error(getClass(), throwableGroup);
            for (Throwable throwable : throwableGroup) {
                if (throwable instanceof DuckableException) {
                    Logger.getInstance().error(throwable.getClass(), throwable);
                    throw (DuckableException) throwable;
                } else {
                    Logger.getInstance().exception(throwable);
                }
            }
        }
    }


}
