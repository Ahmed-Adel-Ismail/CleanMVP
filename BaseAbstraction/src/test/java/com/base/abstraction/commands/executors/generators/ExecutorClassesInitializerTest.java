package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.annotations.interfaces.UpdatesHandler;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Message;
import com.base.abstraction.interfaces.Initializable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 12/1/2016.
 */
public class ExecutorClassesInitializerTest {

    public ExecutorClassesInitializerTest() {
    }

    @Test
    public void name() throws Exception {
        A a = new A();
        Assert.assertTrue(a.initialized);
    }


    @UpdatesHandler({B.class, B.class})
    static class A {
        boolean initialized;

        A() {
            new ClassAnnotationScanner<UpdatesHandler>(UpdatesHandler.class) {
                @Override
                protected void onAnnotationFound(UpdatesHandler annotation) {
                    new ExecutorClassesInitializer(A.this).execute(annotation.value());
                    initialized = true;
                }
            }.execute(this);

        }

    }


    static class B extends Executor<Message> implements Initializable<A> {
        @Override
        public void initialize(A a) {
            System.out.print("a initialized");
        }
    }


}