package com.base.abstraction.commands;

import io.reactivex.functions.Function;

/**
 * a {@link Command} that can be used with RxJava as a {@link Function}
 * <p>
 * Created by Ahmed Adel on 1/19/2017.
 */
public abstract class RxFunction<Parameter, Return> implements
        Command<Parameter, Return>,
        Function<Parameter, Return> {

    @Override
    public final Return apply(Parameter parameter) throws Exception {
        return execute(parameter);
    }

}
