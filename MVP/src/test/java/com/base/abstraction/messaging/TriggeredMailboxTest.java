package com.base.abstraction.messaging;

import org.junit.Assert;

public class TriggeredMailboxTest {

    public TriggeredMailboxTest() {
    }

    
    public void next$$mailboxState_instanceof_ResumedBlockingMailbox() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.next();
        Assert.assertTrue((s.getState() instanceof ResumedTriggeredMailbox));

    }

    
    public void resume$$mailboxState_instanceof_ResumedBlockingMailbox() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.resume();
        Assert.assertTrue((s.getState() instanceof ResumedTriggeredMailbox));

    }

    
    public void pause$$mailboxState_instanceof_PausedBlockingMailbox() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.pause();
        Assert.assertTrue((s.getState() instanceof PausedTriggeredMailbox));
    }

    
    public void execute_1_2$$valueSize_1() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.execute(1).execute(2);
        Assert.assertTrue(e.values.size() == 1);
    }

    
    public void execute_1_next_execute_2_next_execute_3_next$$values_1_2_3() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.execute(1).next().execute(2).next().execute(3).next();
        Assert.assertTrue(e.values.size() == 3
                && e.values.get(0) == 1
                && e.values.get(1) == 2
                && e.values.get(2) == 3);
    }

    
    public void execute_1_2_3_4$next$next$$values_1_2_3() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.execute(1).execute(2).execute(3).execute(4).next().next();
        Assert.assertTrue(e.values.size() == 3
                && e.values.get(0) == 1
                && e.values.get(1) == 2
                && e.values.get(2) == 3);
    }

    
    public void next$execute_1_2$$values_1() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.next().execute(1).execute(2);
        Assert.assertTrue(e.values.size() == 1
                && e.values.get(0) == 1);
    }

    
    public void pause$execute_1_2_3$resume$$values_1() throws Exception {
        Executor e = new Executor();
        TriggeredMailbox<Integer> s = new TriggeredMailbox<>(e);
        s.pause().execute(1).execute(2).execute(3).resume();
        Assert.assertTrue(e.values.size() == 1
                && e.values.get(0) == 1);
    }


}