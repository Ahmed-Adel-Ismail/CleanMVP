package com.base.abstraction.concurrency;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * a {@link Future} but for tasks that will have only two results, either they will be completed,
 * or an {@link Exception} will be thrown, it is simply a {@code Boolean} {@link Future} that
 * only returns {@code true} in it's {@link #onComplete(Command)} {@link Command#execute(Object)}
 * <p>
 * Created by Ahmed Adel on 1/19/2017.
 */
public class FutureTask extends Future<Boolean> {

    public FutureTask() {
    }

    public FutureTask completed() {
        super.setResult(true);
        return this;
    }

    /**
     * set the command that will be executed when {@link #completed()} is invoked, notice
     * that the parameter of this {@link Command} will always receive {@code true} as parameter
     *
     * @param onComplete the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    @Override
    public FutureTask onComplete(Command<Boolean, Void> onComplete) {
        super.onComplete(onComplete);
        return this;
    }

    @Override
    public FutureTask onException(Command<Throwable, Void> onException) {
        super.onException(onException);
        return this;
    }

    @Override
    public FutureTask onThread(@NonNull ExecutionThread executionThread) {
        super.onThread(executionThread);
        return this;
    }

    @Override
    public FutureTask setException(Throwable exception) {
        super.setException(exception);
        return this;
    }

    /**
     * @deprecated use {@link #completed()} instead
     */
    @Deprecated
    @Override
    public FutureTask setResult(Boolean aBoolean) {
        super.setResult(aBoolean);
        return this;
    }

    @Override
    public FutureTask owner(Object owner) {
        super.owner(owner);
        return this;
    }
}
