package com.base.abstraction.messaging;

import android.support.annotation.NonNull;

/**
 * the paused state for {@link TriggeredMailbox}
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
class PausedTriggeredMailbox<Task> extends PausedMailboxState<Task> {

    PausedTriggeredMailbox(MailboxModel<Task> mailboxModel) {
        super(mailboxModel);
    }

    @NonNull
    @Override
    public AbstractMailbox<Task> resume() {
        return new ResumedTriggeredMailbox<>(getMailboxModel());
    }

}
