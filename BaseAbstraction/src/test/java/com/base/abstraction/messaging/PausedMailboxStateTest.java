package com.base.abstraction.messaging;

import org.junit.Assert;
import org.junit.Test;

public class PausedMailboxStateTest {

    public PausedMailboxStateTest() {
    }


    @Test
    public void pause$$mailboxState_instanceof_PausedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause();
        Assert.assertTrue((s.getState() instanceof PausedMailboxState));

    }

    @Test
    public void resume$$mailboxState_instanceof_ResumedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause().resume();
        Assert.assertTrue((s.getState() instanceof ResumedMailboxState));

    }

    @Test
    public void execute_1_2$$tasksValues_1_2() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause().execute(1).execute(2);
        Assert.assertTrue((s.getMailboxModel().getTasks().get(0) == 1
                && s.getMailboxModel().getTasks().get(1) == 2));

    }
}