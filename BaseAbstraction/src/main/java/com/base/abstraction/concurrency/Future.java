package com.base.abstraction.concurrency;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * a class that returns a result in the future, when the result is returned, it will invoke the
 * {@link Command} passed to it's {@link #onComplete(Command)}, if an exception occurred,
 * it will run {@link #onException} ... if {@link #onException} was not setVariable, it will
 * run {@link #onComplete} with a {@code null} parameter
 * <p>
 * notice that when the callback methods are invoked ({@link #onComplete} or {@link #onException}),
 * this Object will clear itself, it wont be usable any more
 * <p>
 * notice also that the callbacks are executed on the thread that created this instance by default,
 * to specify the thread, you should invoke {@link #onThread(ExecutionThread)} before setting the callbacks
 * <p>
 * Created by Ahmed Adel on 11/16/2016.
 *
 * @param <Result> the expected type of the result
 */
public class Future<Result> extends OwnedReference {


    private Result result;
    private Command<Result, Void> onComplete;
    private boolean resultFinished;
    private Throwable exception;
    private Command<Throwable, Void> onException;

    private Disposable disposable;
    private Scheduler executionScheduler;
    private boolean cleared;


    /**
     * create an instance that will run the callbacks on the current Thread, to specify a Thread,
     * you will need to invoke {@link #onThread(ExecutionThread)}
     */
    public Future() {
        executionScheduler = ExecutionThread.CURRENT.scheduler();
    }

    /**
     * set the {@link Looper} for the  {@link WorkerThread} that will host the {@link #onComplete}
     * and {@link #onException} methods ... notice that no {@link WorkerThread} is initialized
     * at this point, the {@link WorkerThread} will be initialized when invoking the
     * {@link Command} that will be executed in {@link #onComplete(Command)} or
     * {@link #onException} ... calling this method <b>after</b>
     *
     * @param executionThread a {@link Looper} to run the {@link #onComplete} and {@link #onException} on
     *                        them, if this parameter was {@code null}, these callbacks will run in a
     *                        background onThread, you can pass {@link Looper#getMainLooper()} to run them
     *                        on the main onThread
     * @return {@code this} instance for chaining
     */
    public Future<Result> onThread(@NonNull ExecutionThread executionThread) {
        executionScheduler = executionThread.scheduler();
        return this;
    }

    /**
     * check if this {@link Future} is cleared or still valid
     *
     * @return {@code true} if this {@link Future} will never take effect
     */
    public boolean isCleared() {
        return cleared;
    }

    /**
     * set the result of this {@link Future}, this is used by the class that is doing long
     * operations, not the class that is waiting for a result
     *
     * @param result the Object to be returned
     * @return {@code this} instance for chaining
     */
    public Future<Result> setResult(Result result) {
        this.result = result;
        this.resultFinished = true;
        return executeResult();
    }

    /**
     * set the {@link Command} that will be executed when the result is returned
     *
     * @param onComplete the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public Future<Result> onComplete(final Command<Result, Void> onComplete) {
        this.onComplete = onComplete;
        return executeResult();
    }

    private Future<Result> executeResult() {
        if (resultFinished && onComplete != null) {
            executeOnComplete();
        }
        return this;
    }


    /**
     * set the {@link Throwable} thrown in this {@link Future},
     * this is used by the class that is doing long operations, not the class that is waiting for
     * a result
     *
     * @param exception the {@link Throwable} that was thrown in the operation
     * @return {@code this} instance for chaining
     */
    public Future<Result> setException(Throwable exception) {
        this.exception = exception;
        return executeException();
    }

    /**
     * set the {@link Command} that will be executed when an exception is thrown
     *
     * @param onException the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public Future<Result> onException(Command<Throwable, Void> onException) {
        this.onException = onException;
        return executeException();
    }


    private Future<Result> executeException() {
        if (exception != null) {
            handleExceptionThrown();
        }
        return this;
    }

    private void handleExceptionThrown() {
        if (onException != null) {
            invokeOnException();
        } else if (onComplete != null) {
            invokeOnCompleteWithNull();
        }
    }

    private void invokeOnCompleteWithNull() {
        Logger.getInstance().exception(exception);
        result = null;
        resultFinished = true;
        executeOnComplete();
    }


    private void executeOnComplete() {
        disposable = Observable.create(createOnCompleteObservable())
                .subscribeOn(executionScheduler).observeOn(Schedulers.trampoline())
                .subscribe(onNextConsumer(), onErrorConsumer(), onCompleteConsumer());
    }

    private void invokeOnException() {
        disposable = Observable.create(createOnExceptionObservable())
                .subscribeOn(executionScheduler).observeOn(Schedulers.trampoline())
                .subscribe(onNextConsumer(), onErrorConsumer(), onCompleteConsumer());
    }


    @NonNull
    private Consumer<Throwable> onNextConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // do nothing
            }
        };
    }

    @NonNull
    private Consumer<Throwable> onErrorConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.getInstance().exception(throwable);
            }
        };
    }

    @NonNull
    private Action onCompleteConsumer() {
        return new Action() {
            @Override
            public void run() throws Exception {
                clear();
            }
        };
    }


    @NonNull
    private ObservableOnSubscribe<Throwable> createOnCompleteObservable() {
        return new ObservableOnSubscribe<Throwable>() {

            @Override
            public void subscribe(ObservableEmitter<Throwable> e) throws Exception {
                if (onComplete != null && resultFinished) {
                    try {
                        onComplete.execute(result);
                        e.onNext(new RuntimeException("Null"));
                    } catch (Throwable ex) {
                        e.onError(ex);
                    } finally {
                        e.onComplete();
                    }
                } else {
                    e.onError(new UnsupportedOperationException("invoking onComplete" +
                            "and no result or onCompleteCommand found"));
                }
            }
        };
    }

    @NonNull
    private ObservableOnSubscribe<Throwable> createOnExceptionObservable() {
        return new ObservableOnSubscribe<Throwable>() {

            @Override
            public void subscribe(ObservableEmitter<Throwable> e) throws Exception {
                if (onException != null && exception != null) {
                    try {
                        onException.execute(exception);
                        e.onNext(exception);
                    } catch (Throwable ex) {
                        e.onError(ex);
                    } finally {
                        e.onComplete();
                    }
                } else {
                    e.onError(new UnsupportedOperationException("invoking onException" +
                            "and no exception or onExceptionCommand found"));
                }
            }
        };
    }


    @Override
    public Future<Result> owner(Object owner) {
        super.owner(owner);
        return this;
    }

    public void clear() {
        super.clear();
        if (disposable != null) {
            clearDisposable();
        }
        onComplete = null;
        onException = null;
        result = null;
        resultFinished = true;
        exception = null;
        executionScheduler = null;
        cleared = true;
    }

    private void clearDisposable() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

}
