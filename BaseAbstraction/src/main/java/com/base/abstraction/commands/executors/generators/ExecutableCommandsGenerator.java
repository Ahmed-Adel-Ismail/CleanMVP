package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.logs.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * a class that reads the annotations of {@link ExecutableCommand} in an instance and adds all these
 * methods in a {@link Executor} Object
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 *
 * @see ExecutorFromMethodsGenerator
 */
public class ExecutableCommandsGenerator<Parameter> extends
        ExecutorFromMethodsGenerator<ExecutableCommand, Parameter> {

    /**
     * create a {@link ExecutableCommandsGenerator} instance
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    public ExecutableCommandsGenerator(Executor<Parameter> executor) {
        super(executor, ExecutableCommand.class);
    }

    @Override
    protected void processMethodAnnotation(
            Executor<Parameter> executor,
            Method method,
            ExecutableCommand annotation) {
        try {
            putInExecutor(executor, method, annotation);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            new TestException().execute(e);
        }

    }

    @SuppressWarnings("unchecked")
    private void putInExecutor(
            Executor<Parameter> executor,
            Method method,
            ExecutableCommand annotation)
            throws IllegalAccessException, InvocationTargetException {

        method.setAccessible(true);
        Command<Parameter, ?> command;
        command = (Command<Parameter, ?>) method.invoke(getClientObject());

        if (annotation.value().length == 1) {
            executor.put(annotation.value()[0], command);
        } else {
            for (long id : annotation.value()) {
                executor.put(id, command);
            }
        }
    }
}
