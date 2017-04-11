package com.base.abstraction.concurrency;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.interfaces.Clearable;

/**
 * a {@link Future} that can be invoked multiple times, this class uses the {@link Future} Object
 * but it is <b>NOT</b> a {@link Future} Object
 * <p>
 * you can call {@link #clear()} to free resources, but this is not mandatory
 * <p>
 * Created by Ahmed Adel on 1/19/2017.
 */
public class FutureQue<Result> implements Clearable {

    private Command<Result, Void> onComplete;
    private Command<Throwable, Void> onException;
    private ExecutionThread executionThread;
    private Future<Result> future;

    public FutureQue() {

    }

    /**
     * like {@link Future#onComplete(Command)} but this will be executed multiple times
     *
     * @param onNext the {@link Command} that will be executed every time a result is setVariable
     * @return {@code this} instance for chaining
     */
    public FutureQue<Result> onNext(Command<Result, Void> onNext) {
        this.onComplete = onNext;
        updateCurrentFuture();
        return this;
    }

    public FutureQue<Result> onException(Command<Throwable, Void> onException) {
        this.onException = onException;
        updateCurrentFuture();
        return this;
    }

    public FutureQue<Result> onThread(@NonNull ExecutionThread executionThread) {
        this.executionThread = executionThread;
        updateCurrentFuture();
        return this;
    }

    public FutureQue<Result> setException(Throwable exception) {
        updateCurrentFuture();
        future.setException(exception);
        return this;
    }

    public FutureQue<Result> setResult(Result result) {
        updateCurrentFuture();
        future.setResult(result);
        return this;
    }

    private void updateCurrentFuture() {
        if (future == null || future.isCleared()) {
            future = new Future<>();
        }
        future.onThread(executionThread).onComplete(onComplete).onException(onException);
    }


    @Override
    public void clear() {
        if (future != null) {
            future.clear();
            future = null;
        }
        onComplete = null;
        onException = null;
        executionThread = null;
    }
}
