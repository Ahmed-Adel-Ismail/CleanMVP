package com.base.abstraction.commands;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutableCommandsGenerator;
import com.base.abstraction.commands.executors.generators.ExecutableGenerator;
import com.base.abstraction.commands.executors.generators.ExecutorGenerator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ahmed Adel on 11/24/2016.
 */
public class ExecutorTest {

    public ExecutorTest() {
    }

    @Test
    public void execute_1_2$$oneExecuted_twoExecuted() throws Exception {
        ExecutorSubclass e = new ExecutorSubclass();
        e.execute(1L, "A");
        e.execute(2L, "B");
        Assert.assertTrue(e.oneExecuted && e.twoExecuted);
    }


    @Load
    private static class ExecutorSubclass extends Executor<String> {

        private boolean oneExecuted;
        private boolean twoExecuted;

        @Executable(1)
        void createCommandOne(String s) {
            oneExecuted = true;
            System.out.println("1 : " + s);

        }

        @Executable(2)
        void createCommandTwo(String s) {
            twoExecuted = true;
            System.out.println("2 : " + s);

        }

    }


    @Test
    public void execute_C1_C2_C3_C4_C5$$oneExecuted_twoExecuted_threeExecuted_fourExecuted() throws Exception {
        C c = new C();
        c.executor.execute(1L, 1000);
        c.executor.execute(2L, 2000);
        c.executor.execute(3L, 3000);
        c.executor.execute(4L, 4000);
        c.executor.execute(5L, 5000);
        Assert.assertTrue(c.oneExecuted && c.d.twoExecuted && c.threeExecuted && c.d.fourExecuted);

    }

    public static class C {

        private boolean oneExecuted;
        private boolean threeExecuted;

        Executor<Integer> executor;

        @com.base.abstraction.annotations.interfaces.Executor
        D d;

        public C() {
            executor = new Executor<>();
            executor = new ExecutableGenerator<Integer>(executor).execute(this);
            executor = new ExecutableCommandsGenerator<Integer>(executor).execute(this);
            executor = new ExecutorGenerator<Integer>(executor).execute(this);
        }

        @Executable({1, 5, 6})
        void printOne(Integer i) {
            System.out.println("C1 : " + i);
            oneExecuted = true;
        }

        @ExecutableCommand(2)
        Command<Integer, Void> printThree() {
            return new Command<Integer, Void>() {
                @Override
                public Void execute(Integer i) {
                    System.out.println("C3 : " + i);
                    threeExecuted = true;
                    return null;
                }
            };
        }


    }


    @Load
    public static class D extends Executor<Integer> {

        private boolean twoExecuted;
        private boolean fourExecuted;

        public D() {
            System.out.println("D created");
        }

        @Executable(3)
        void printTwo(Integer i) {
            System.out.println("D2 : " + i);
            twoExecuted = true;
        }

        @ExecutableCommand(4)
        Command<Integer, Void> printFour() {
            return new Command<Integer, Void>() {
                @Override
                public Void execute(Integer i) {
                    System.out.println("D4 : " + i);
                    fourExecuted = true;
                    return null;
                }
            };
        }

    }

}