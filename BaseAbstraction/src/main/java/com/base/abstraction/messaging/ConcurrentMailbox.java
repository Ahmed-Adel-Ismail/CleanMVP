package com.base.abstraction.messaging;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.failures.Failure;
import com.base.abstraction.logs.Logger;

import java.util.LinkedList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;

/**
 * a mailbox that is implemented with RxJava Observables
 * <p>
 * Created by Ahmed Adel on 2/8/2017.
 */
public class ConcurrentMailbox<Task> extends AbstractMailbox<Task> {


    private boolean stopped;
    private ReplaySubject<Task> observable;
    private Disposable disposable;
    private Task lastTask;


    public ConcurrentMailbox(@NonNull Command<Task, ?> executor, Looper looper) {
        super(createMailboxModel(executor, looper).name(executor.getClass().getName()));
        initializeAndExecuteTaskHistoryIfRequired();
    }

    public ConcurrentMailbox(@NonNull Command<Task, ?> executor) {
        this(executor, null);
        initializeAndExecuteTaskHistoryIfRequired();
    }


    @NonNull
    @Override
    public ConcurrentMailbox<Task> resume() {
        stopped = false;
        initializeAndExecuteTaskHistoryIfRequired();
        return this;
    }

    private void initializeAndExecuteTaskHistoryIfRequired() {

        if (disposable != null) {
            Logger.getInstance().info(getMailboxModel().getName(), "disposable already initialized");
            return;
        }

        Scheduler scheduler = AndroidSchedulers.from(getWorkerThread().getExecutionLooper());
        observable = ReplaySubject.createWithSize(5);
        observable.subscribeOn(scheduler).observeOn(scheduler);
        if (!getTaskHistory().isEmpty()) {
            executePendingTasks();
        }
        disposable = observable.subscribe(onExecute());

    }

    private void executePendingTasks() {
        LinkedList<Task> taskHistoryCopy = new LinkedList<>(getTaskHistory());
        getTaskHistory().clear();

        for (Task task : taskHistoryCopy) {
            observable.onNext(task);
        }

    }

    @NonNull
    @Override
    public ConcurrentMailbox<Task> pause() {
        stopped = true;
        clearDisposable();
        return this;
    }

    @Override
    public ConcurrentMailbox<Task> execute(Task task) {
        if (stopped) {
            getTaskHistory().add(task);
        } else {
            lastTask = task;
            observable.onNext(task);
        }
        return this;
    }

    @Override
    public void clear() {
        clearDisposable();
        super.clear();

    }


    @NonNull
    private Consumer<Task> onExecute() {
        return new Consumer<Task>() {
            @Override
            public void accept(Task task) throws Exception {
                onExecuteImplementation(task);
            }
        };
    }

    private void onExecuteImplementation(Task task) {
        Command<Task, ?> executor = getExecutor();
        if (executor != null) {
            doExecute(task, executor);
        } else {
            Logger.getInstance().error(getMailboxModel().getName(), "null @ executor");
        }

    }

    private void doExecute(Task task, Command<Task, ?> executor) {
        boolean completed = false;
        try {
            executor.execute(task);
            completed = true;
        } catch (Failure e) {
            Logger.getInstance().exception(e);
            throw e;
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        } finally {
            if (!completed) {
                getTaskHistory().addFirst(lastTask);
                lastTask = null;
            }
        }
    }


    private void clearDisposable() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            disposable = null;
            observable = null;
        }
    }
}
