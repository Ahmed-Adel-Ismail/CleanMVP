package com.base.abstraction.messaging;

import junit.framework.Assert;


public class MailboxTestConcurrent {

    private static final int SLEEP_MILLIS = 2000;

    public MailboxTestConcurrent() {
    }


    
    public void execute$$valuesSize_20() throws Exception {
        Executor e = new Executor();
        final Mailbox<Integer> s = new Mailbox<>(e);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    s.execute(i);
                }
            }
        });
        thread.run();
        for (int i = 100; i < 110; i++) {
            s.execute(i);
        }

        Thread.sleep(SLEEP_MILLIS);
        Assert.assertTrue(e.values.size() == 20);

    }

    
    public void pause$$valuesSize_not_20() throws Exception {
        Executor e = new Executor();
        final Mailbox<Integer> s = new Mailbox<>(e);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                s.pause();
                for (int i = 0; i < 10; i++) {
                    s.execute(i);
                }
            }
        });
        thread.run();
        for (int i = 100; i < 110; i++) {
            s.execute(i);
        }

        Thread.sleep(SLEEP_MILLIS);
        Assert.assertTrue(e.values.size() != 20);

    }

    
    public void pause$resume$pause$resume$$valuesSize_20() throws Exception {
        Executor e = new Executor();
        final Mailbox<Integer> s = new Mailbox<>(e);
        s.pause();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    s.execute(i);
                    s.resume();
                }
            }
        });
        thread.run();
        for (int i = 100; i < 110; i++) {
            s.pause();
            s.execute(i);
        }

        Thread.sleep(SLEEP_MILLIS);
        s.resume();
        Assert.assertTrue(e.values.size() == 20);

    }


}