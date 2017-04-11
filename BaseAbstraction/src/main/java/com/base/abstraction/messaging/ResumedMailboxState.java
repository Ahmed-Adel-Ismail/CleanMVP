package com.base.abstraction.messaging;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.RunnableExecutor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;

import java.util.NoSuchElementException;

/**
 * the active state for a {@link Mailbox}
 * <p>
 * Created by Ahmed Adel on 10/20/2016.
 */
class ResumedMailboxState<Task> extends AbstractMailbox<Task> {

    private boolean executing;
    private boolean abortExecution;
    private Command<Task, Void> executeCommand;


    ResumedMailboxState(MailboxModel<Task> mailboxModel) {
        super(mailboxModel);
        executeCommand = createExecuteCommand();
        executePendingTasks();
    }

    @NonNull
    private Command<Task, Void> createExecuteCommand() {
        return new Command<Task, Void>() {
            @Override
            public Void execute(Task task) {
                try {
                    getMailboxModel().getExecutor().execute(task);
                    onExecuteLastTask();
                } catch (CheckedReferenceClearedException e) {
                    Logger.getInstance().error(Mailbox.class, e);
                }
                return null;
            }
        };
    }


    @NonNull
    @Override
    public AbstractMailbox<Task> resume() {
        abortExecution = false;
        executePendingTasks();
        return this;
    }

    @NonNull
    @Override
    public AbstractMailbox<Task> pause() {
        if (executing) {
            abortExecution = true;
            executing = false;
        }
        return new PausedMailboxState<>(getMailboxModel());
    }

    @Override
    public AbstractMailbox<Task> execute(Task task) {
        getMailboxModel().getTasks().add(task);
        if (!executing) {
            executePendingTasks();
        }
        return this;
    }

    @Override
    public void clear() {
        abortExecution = true;
    }


    private void executePendingTasks() {
        while (!abortExecution && (executing = !getMailboxModel().getTasks().isEmpty())) {
            if (!isTaskExecutedSuccessfully()) break;
        }
        updateExecutionAbortedFlags();
    }

    private boolean isTaskExecutedSuccessfully() {
        boolean aborted = abortExecution;
        if (!aborted) {
            executeLastTask();
        } else {
            updateExecutionAbortedFlags();
        }
        return !aborted;
    }

    private void executeLastTask() {
        try {
            Task task = getMailboxModel().getTasks().poll();
            getMailboxModel().getTaskHistory().clear();
            getMailboxModel().getTaskHistory().add(task);
            Runnable runnable = new RunnableExecutor<Task, Void>(task).command(executeCommand);
            getMailboxModel().getWorkerThread().execute(runnable);
        } catch (NoSuchElementException e) {
            Logger.getInstance().exception(e);
        }


    }


    void onExecuteLastTask() {
        // template method
    }

    private void updateExecutionAbortedFlags() {
        executing = false;
        abortExecution = false;
    }

}
