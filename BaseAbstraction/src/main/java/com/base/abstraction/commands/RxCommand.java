package com.base.abstraction.commands;

import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;

import io.reactivex.functions.Consumer;

/**
 * a {@link Command} class that is used by RxJava, it is safe to use {@link CheckedReference}
 * inside the {@link #execute(Object)} method, since the {@link CheckedReferenceClearedException}
 * is handled by default
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public abstract class RxCommand<Parameter> implements
        Command<Parameter, Void>,
        Consumer<Parameter> {

    @Override
    public final void accept(Parameter parameter) throws Exception {
        try {
            execute(parameter);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }
    }

    /**
     * this method is invoked in {@link Consumer#accept(Object)},
     * it is safe to use {@link CheckedReference}
     * inside this method, since the {@link CheckedReferenceClearedException}
     * is handled by default
     *
     * @param p the parameter to be used
     * @return always returns {@code null}
     */
    @Override
    public abstract Void execute(Parameter p);
}
