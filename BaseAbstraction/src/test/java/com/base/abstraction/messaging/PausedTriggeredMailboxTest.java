package com.base.abstraction.messaging;

import org.junit.Assert;
import org.junit.Test;

public class PausedTriggeredMailboxTest {

    public PausedTriggeredMailboxTest() {
    }

    @Test
    public void resume$$mailboxState_instanceof_ResumedBlockingMailbox() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.pause().resume();
        Assert.assertTrue((s.getState() instanceof ResumedTriggeredMailbox));

    }

}