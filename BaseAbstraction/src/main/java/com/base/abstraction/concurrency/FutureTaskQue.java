package com.base.abstraction.concurrency;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * a {@link FutureQue} but for tasks that will have only two results, either they will be next,
 * or an {@link Exception} will be thrown, it is simply a {@code Boolean} {@link FutureQue} that
 * only returns {@code true} in it's {@link #onNext(Command)}  {@link Command#execute(Object)}
 * <p>
 * Created by Ahmed Adel on 1/19/2017.
 */
public class FutureTaskQue extends FutureQue<Boolean> {

    /**
     * same as {@link #setResult(Boolean)}, but this method handles passing the parameter
     *
     * @return {@code this} instance for chaining
     */
    public FutureTaskQue next() {
        super.setResult(true);
        return this;
    }

    /**
     * set the command that will be executed when {@link #next()} is invoked, notice
     * that the parameter of this {@link Command} will always receive {@code true} as parameter
     *
     * @param onComplete the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    @Override
    public FutureTaskQue onNext(Command<Boolean, Void> onComplete) {
        super.onNext(onComplete);
        return this;
    }

    @Override
    public FutureTaskQue onException(Command<Throwable, Void> onException) {
        super.onException(onException);
        return this;
    }

    @Override
    public FutureTaskQue onThread(@NonNull ExecutionThread executionThread) {
        super.onThread(executionThread);
        return this;
    }

    @Override
    public FutureTaskQue setException(Throwable exception) {
        super.setException(exception);
        return this;
    }

    /**
     * @deprecated use {@link #next()}  instead
     */
    @Override
    public FutureTaskQue setResult(Boolean aBoolean) {
        super.setResult(aBoolean);
        return this;
    }


}
