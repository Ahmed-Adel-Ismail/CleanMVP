package com.base.abstraction.messaging;

import org.junit.Assert;
import org.junit.Test;

public class ResumedMailboxStateTest {

    public ResumedMailboxStateTest() {
    }


    @Test
    public void resume$$mailboxState_instanceof_ResumedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume();
        Assert.assertTrue((s.getState() instanceof ResumedMailboxState));

    }

    @Test
    public void pause$$mailboxState_instanceof_PausedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume().pause();
        Assert.assertTrue((s.getState() instanceof PausedMailboxState));
    }

    @Test
    public void execute_1_2$$tasksSize_0() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume().execute(1).execute(2);
        Assert.assertTrue((s.getMailboxModel().getTasks().size() == 0));

    }
}