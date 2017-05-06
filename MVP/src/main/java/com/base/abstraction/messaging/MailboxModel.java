package com.base.abstraction.messaging;

import android.os.Looper;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.WorkerThread;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.interfaces.Clearable;

import java.util.LinkedList;

/**
 * the Object that creates a {@link AbstractMailbox}
 * <p>
 * Created by Ahmed Adel on 11/15/2016.
 */
class MailboxModel<Task> implements Clearable {

    private String name;
    private Command<Task, ?> executor;
    private LinkedList<Task> tasks;
    private LinkedList<Task> taskHistory;
    private WorkerThread workerThread;

    /**
     * create a {@link Mailbox} on a {@link android.os.HandlerThread} or a {@link Thread} that
     * contains a {@link Looper} ... mainly used to invoke the {@link #getExecutor()} on the
     * {@link Looper#getMainLooper()}
     *
     * @param executor the {@link Command} that will be executed
     * @param looper   the {@link Looper}
     */
    MailboxModel(Command<Task, ?> executor, Looper looper) {
        this.executor = executor;
        this.workerThread = new WorkerThread(looper);
    }

    MailboxModel<Task> tasks(LinkedList<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    MailboxModel<Task> taskHistory(LinkedList<Task> taskHistory) {
        this.taskHistory = taskHistory;
        return this;
    }

    LinkedList<Task> getTaskHistory() {
        return taskHistory;
    }

    /**
     * get the queue of tasks that need to be executed
     *
     * @return a group of tasks, this can be empty if no tasks are remaining, but it will never be
     * {@code null}
     */
    LinkedList<Task> getTasks() {
        return tasks;
    }

    WorkerThread getWorkerThread() {
        return workerThread;
    }


    Command<Task, ?> getExecutor() throws CheckedReferenceClearedException {
        if (executor != null) {
            return executor;
        }
        throw new CheckedReferenceClearedException();
    }

    @Override
    public void clear() {
        workerThread.clear();

        if (taskHistory != null) {
            taskHistory.clear();
        }
        if (tasks != null) {
            tasks.clear();
        }

        executor = null;
    }

    public MailboxModel<Task> name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
