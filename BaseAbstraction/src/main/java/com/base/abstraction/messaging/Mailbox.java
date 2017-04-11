package com.base.abstraction.messaging;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * A class that is responsible for scheduling tasks for an executor Object, where it lets
 * this executor to {@link #pause()} execution while it keeps receiving tasks, and when
 * it's {@link #resume()} is invoked, all it's pending tasks are invoked
 * <p>
 * Created by Ahmed Adel on 10/19/2016.
 *
 * @param <Task> the type of the task {@code Object} that will be passed to the
 *               {@link Command#execute(Object)} parameter
 */
public class Mailbox<Task> extends AbstractMailbox<Task> {

    /**
     * create a {@link Mailbox} to manage tasks handling in a different {@link Thread}
     * {@link Looper}
     *
     * @param executor the {@link Command} that holds the true implementation of handling the
     *                 task
     */
    public Mailbox(@NonNull Command<Task, ?> executor) {
        super(createMailboxModel(executor, null));
        changeState(new ResumedMailboxState<>(getMailboxModel()));
    }


    /**
     * create a {@link Mailbox} to manage tasks handling in a given {@link Thread} {@link Looper}
     *
     * @param executor the {@link Command} that holds the true implementation of handling the
     *                 task
     * @param looper   the {@link Looper} that will have the {@link Command} executed upon
     */
    public Mailbox(@NonNull Command<Task, ?> executor, Looper looper) {
        super(createMailboxModel(executor, looper));
        changeState(new ResumedMailboxState<>(getMailboxModel()));
    }


    /**
     * pause the current {@link Mailbox}, this will cause all the coming tasks through
     * {@link #execute(Object)} will  be saved in a queue, until {@link #resume()} is invoked
     *
     * @return {@code this} instance for chaining
     */
    @NonNull
    public Mailbox<Task> pause() {
        changeState(getState().pause());
        return this;
    }

    @Override
    public Mailbox<Task> push(Task task) {
        super.push(task);
        return this;
    }

    /**
     * resume the activity of this {@link Mailbox}, this will cause any pending tasks (previously
     * requested through invoking {@link #execute(Object)} to be executed immediately)
     *
     * @return {@code this} instance for chaining
     */
    @NonNull
    public Mailbox<Task> resume() {
        changeState(getState().resume());
        return this;
    }

    /**
     * execute a task, if this {@link Mailbox} is paused, it will save this task in a
     * queue, and when {@link #resume()} is called, all the saved tasks will be executed
     * in the same order they were requested
     *
     * @param task a task to be executed
     * @return {@code this} instance for chaining
     */
    @Override
    public Mailbox<Task> execute(Task task) {
        changeState(getState().execute(task));
        return this;
    }

}
