package com.base.abstraction.messaging;

import org.junit.Assert;

public class ResumedTriggeredMailboxTest {


    public ResumedTriggeredMailboxTest() {
    }


    public void resume$$mailboxState_instanceof_ResumedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume();
        Assert.assertTrue((s.getState() instanceof ResumedMailboxState));

    }

    public void pause$$mailboxState_instanceof_PausedBlockingMailbox() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.resume().pause();
        Assert.assertTrue((s.getState() instanceof PausedTriggeredMailbox));

    }

    public void execute_1_2$$values_1() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.resume().execute(1).execute(2);
        Assert.assertTrue(e.values.size() == 1 && e.values.get(0) == 1);
    }
}