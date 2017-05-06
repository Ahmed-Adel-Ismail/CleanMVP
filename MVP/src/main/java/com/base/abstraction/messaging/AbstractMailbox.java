package com.base.abstraction.messaging;

import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.base.abstraction.actors.base.ThreadHosted;
import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.WorkerThread;
import com.base.abstraction.exceptions.NoLooperException;
import com.base.abstraction.interfaces.Clearable;

import java.util.LinkedList;

/**
 * a class that represents the state of a {@link Mailbox}
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 */
public abstract class AbstractMailbox<Task> implements
        ThreadHosted,
        Command<Task, AbstractMailbox<Task>>,
        Clearable {


    private final MailboxModel<Task> mailboxModel;
    private AbstractMailbox<Task> state;

    AbstractMailbox(MailboxModel<Task> mailboxModel) {
        this.mailboxModel = mailboxModel;
    }


    @Override
    public long getThreadId() {
        if (mailboxModel.getWorkerThread() != null) {
            return mailboxModel.getWorkerThread().getLooper().getThread().getId();
        }
        throw new NoLooperException(this);
    }


    @Override
    @CallSuper
    public void clear() {
        if (state != null) {
            state.clear();
        }
        if (mailboxModel != null) {
            mailboxModel.clear();
        }
    }

    /**
     * pause the current {@link Mailbox}, this will cause all the coming tasks through
     * {@link Command#execute(Object)} will  be saved in a queue, until {@link #resume()} is invoked
     *
     * @return {@code this} instance for chaining
     */
    @NonNull
    public abstract AbstractMailbox<Task> pause();

    /**
     * if this {@link Mailbox} is paused, you can add a task to be picked first when
     * {@link #resume()} is invoked, this method puts a task to the front of the pending
     * tasks, if the {@link Mailbox} is already executing tasks, this task will become the
     * next task when calling {@link #execute(Object)}
     *
     * @param task the task to be put at the front of the pending tasks
     * @return {@code this} instance for chaining
     */
    public AbstractMailbox<Task> push(Task task) {
        mailboxModel.getTasks().push(task);
        return this;
    }

    /**
     * resume the activity of this {@link Mailbox}, this will cause any pending tasks (previously
     * requested through invoking {@link Command#execute(Object)} to be executed immediately
     *
     * @return {@code this} instance for chaining
     */
    @NonNull
    public abstract AbstractMailbox<Task> resume();

    /**
     * get the tasks history, every executed task is passed to this group so that it can be reached
     * later
     *
     * @return a group of tasks that were executed before, the last task executed is at the
     * top of this group
     */
    public final LinkedList<Task> getTaskHistory() {
        return mailboxModel.getTaskHistory();
    }

    AbstractMailbox<Task> getState() {
        return state;
    }

    WorkerThread getWorkerThread() {
        return mailboxModel.getWorkerThread();
    }

    AbstractMailbox<Task> changeState(AbstractMailbox<Task> state) {
        return this.state = state;
    }

    Command<Task, ?> getExecutor() {
        return mailboxModel.getExecutor();
    }

    MailboxModel<Task> getMailboxModel() {
        return mailboxModel;
    }


    static <Task> MailboxModel<Task> createMailboxModel(
            @NonNull Command<Task, ?> executor,
            Looper looper) {

        return new MailboxModel<>(executor, looper).tasks(
                new LinkedList<Task>())
                .taskHistory(new LinkedList<Task>());
    }
}
