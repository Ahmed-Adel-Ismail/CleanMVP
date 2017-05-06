package com.base.abstraction.concurrency;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;

/**
 * a {@link Runnable} class that is used to execute to the {@link Command} for
 * {@link WorkerThread} or in any method that takes a {@link Runnable}
 * <p>
 * Created by Ahmed Adel on 11/15/2016.
 */
public class RunnableExecutor<Parameter, Return> implements Runnable, Command<Parameter, Void> {

    private Parameter parameter;
    private Command<Parameter, Return> command;
    private Command<Boolean, ?> onFinishCommand;

    @SuppressWarnings("WeakerAccess")
    public RunnableExecutor() {

    }

    @SuppressWarnings("WeakerAccess")
    public RunnableExecutor(Parameter parameter) {
        this();
        this.parameter = parameter;
    }

    public RunnableExecutor<Parameter, Return> parameter(Parameter parameter) {
        this.parameter = parameter;
        return this;
    }

    public RunnableExecutor<Parameter, Return> command(Command<Parameter, Return> command) {
        this.command = command;
        return this;
    }

    public RunnableExecutor<Parameter, Return> onFinish(Command<Boolean, ?> command) {
        this.onFinishCommand = command;
        return this;
    }


    @Override
    public final void run() {
        boolean completedSuccessfully = false;
        try {
            execute(parameter);
            completedSuccessfully = true;
        } catch (Throwable e) {
            new TestException().execute(e);
        } finally {
            if (onFinishCommand != null) {
                onFinishCommand.execute(completedSuccessfully);
            }
            command = null;
        }
    }


    @Override
    public Void execute(Parameter parameter) {
        try {
            command.execute(parameter);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return null;
    }

    Command<Parameter, Return> getCommand() {
        return command;
    }


}