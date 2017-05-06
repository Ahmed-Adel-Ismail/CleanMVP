package com.base.abstraction.messaging;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * a {@link Mailbox} that contains tasks that should block the upcoming tasks unless the
 * current message is done ... this means that it will hold to the tasks received, but it will
 * {@link #pause()} itself after every time a task is executed, you should call {@link #resume()}
 * manually for this class to continue
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
public class TriggeredMailbox<Task> extends Mailbox<Task> {

    /**
     * create a {@link TriggeredMailbox} to manage tasks handling in a different {@link Thread}
     * {@link Looper}
     *
     * @param executor the {@link Command} that holds the true implementation of handling the
     *                 task
     */
    public TriggeredMailbox(@NonNull Command<Task, ?> executor) {
        super(executor);
        changeState(new ResumedTriggeredMailbox<>(getMailboxModel()));
    }

    public TriggeredMailbox(String name, @NonNull Command<Task, ?> executor) {
        this(executor);
        getMailboxModel().name(name);
    }

    /**
     * create a {@link TriggeredMailbox} to manage tasks handling in a given {@link Thread}
     * {@link Looper}
     *
     * @param executor the {@link Command} that holds the true implementation of handling the
     *                 task
     * @param looper   the {@link Looper} that will have the {@link Command} executed upon
     */
    public TriggeredMailbox(@NonNull Command<Task, ?> executor, Looper looper) {
        super(executor, looper);
        changeState(new ResumedTriggeredMailbox<>(getMailboxModel()));
    }

    /**
     * same as {@link #resume()}, but you should call this method every time a task is executed so
     * for this {@link TriggeredMailbox} to proceed with the next task ... alternatively, you can
     * call {@link #resume()} ... this method is there for descriptive naming only, nothing
     * new
     *
     * @return {@code this} instance for chaining
     */
    public TriggeredMailbox<Task> next() {
        return resume();
    }

    @NonNull
    @Override
    public TriggeredMailbox<Task> resume() {
        super.resume();
        return this;
    }

    @NonNull
    @Override
    public TriggeredMailbox<Task> pause() {
        super.pause();
        return this;
    }

    @Override
    public TriggeredMailbox<Task> push(Task task) {
        super.push(task);
        return this;
    }


    @Override
    public TriggeredMailbox<Task> execute(Task task) {
        super.execute(task);
        return this;
    }
}
