package com.base.abstraction.concurrency;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;

/**
 * a {@link RunnableExecutor} that returns it's result in a {@link Future}, to access the
 * {@link Future} associated with this task, you will need to invoke {@link #getFuture()}
 * <p>
 * Created by Ahmed Adel on 11/16/2016.
 */
@SuppressWarnings("WeakerAccess")
public class FutureRunnableExecutor<Parameter, Return> extends RunnableExecutor<Parameter, Return> {

    @NonNull
    private final Future<Return> future;


    public FutureRunnableExecutor(@NonNull Future<Return> future) {
        this.future = future;
    }

    @Override
    public FutureRunnableExecutor<Parameter, Return> parameter(Parameter parameter) {
        super.parameter(parameter);
        return this;
    }

    @Override
    public FutureRunnableExecutor<Parameter, Return> command(Command<Parameter, Return> command) {
        super.command(command);
        return this;
    }

    @Override
    public Void execute(Parameter parameter) {
        try {
            future.setResult(getCommand().execute(parameter));
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            future.setException(e);
        }
        return null;
    }

}
