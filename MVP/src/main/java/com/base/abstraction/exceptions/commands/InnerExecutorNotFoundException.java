package com.base.abstraction.exceptions.commands;

import com.base.abstraction.commands.executors.Executor;

/**
 * an exception that is thrown if the {@link Executor} was not found as an inner executor in
 * another {@link Executor}
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 */
public class InnerExecutorNotFoundException extends RuntimeException {

    public InnerExecutorNotFoundException(Class<? extends Executor<?>> executorClass) {
        super(executorClass.getSimpleName());
    }
}
