package com.base.abstraction.messaging;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;

/**
 * the resumed state for {@link TriggeredMailbox}
 * <p>
 * Created by Ahmed Adel on 10/24/2016.
 */
class ResumedTriggeredMailbox<Executor extends Command<Task, ?>, Task> extends
        ResumedMailboxState<Task> {


    ResumedTriggeredMailbox(MailboxModel<Task> mailboxModel) {
        super(mailboxModel);
    }

    @NonNull
    @Override
    public AbstractMailbox<Task> pause() {
        super.pause();
        return new PausedTriggeredMailbox<>(getMailboxModel());
    }

    @Override
    void onExecuteLastTask() {
        // this line causes a bug with multiple repositories in one model :
//        clear();
    }
}
