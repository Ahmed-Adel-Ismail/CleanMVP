package com.base.abstraction.messaging;

import android.support.annotation.NonNull;

/**
 * the paused state for a {@link Mailbox}
 * <p>
 * Created by Ahmed Adel on 10/20/2016.
 */
class PausedMailboxState<Task> extends AbstractMailbox<Task> {


    PausedMailboxState(MailboxModel<Task> mailboxModel) {
        super(mailboxModel);
    }

    @NonNull
    @Override
    public AbstractMailbox<Task> pause() {
        return this;
    }

    @NonNull
    @Override
    public AbstractMailbox<Task> resume() {
        return new ResumedMailboxState<>(getMailboxModel());
    }

    @Override
    public AbstractMailbox<Task> execute(Task task) {
        getMailboxModel().getTasks().add(task);
        return this;
    }

    @Override
    public void clear() {
        super.clear();
        // do nothing
    }
}
