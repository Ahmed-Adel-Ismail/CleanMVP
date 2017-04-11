package com.base.abstraction.messaging;

import junit.framework.Assert;


public class MailboxTest {


    public MailboxTest() {
    }


    
    public void execute_1_2$$valuesSize_2() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.execute(1).execute(2);
        Assert.assertTrue(e.values.size() == 2);
    }

    
    public void pause$$valuesSize_0() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause().execute(1).execute(2);
        Assert.assertTrue(e.values.size() == 0);
    }

    
    public void pause$execute_1_2_3$resume$$valuesSize_3() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause().execute(1).execute(2).execute(3).resume();
        Assert.assertTrue(e.values.size() == 3);
    }

    
    public void resume$$valuesSize_0() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume();
        Assert.assertTrue(e.values.size() == 0);
    }

    
    public void pause$execute_1_2$resume$execute_3$$valueSize_3() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause().execute(1).execute(2).resume().execute(3);
        Assert.assertTrue(e.values.size() == 3);
    }

    
    public void resume$execute_1$resume$execute_2$$valueSize_2() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume().execute(1).resume().execute(2);
        Assert.assertTrue(e.values.size() == 2);

    }


    
    public void execute_1$pause$execute_2$push_1$resume$$values_1_1_2() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.execute(1).pause().execute(2).push(1).resume();
        Assert.assertTrue(e.values.size() == 3
                && e.values.get(0) == 1
                && e.values.get(1) == 1
                && e.values.get(2) == 2);


    }

    
    public void push_2$push_1$execute_3$$values_1_2_3() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.push(2).push(1).execute(3);
        Assert.assertTrue(e.values.size() == 3
                && e.values.get(0) == 1
                && e.values.get(1) == 2
                && e.values.get(2) == 3);


    }

    
    public void resume$$mailboxState_instanceof_ResumedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.resume();
        Assert.assertTrue((s.getState() instanceof ResumedMailboxState));

    }

    
    public void pause$$mailboxState_instanceof_PausedMailboxState() throws Exception {
        Executor e = new Executor();
        Mailbox<Integer> s = new Mailbox<>(e);
        s.pause();
        Assert.assertTrue((s.getState() instanceof PausedMailboxState));
    }


}