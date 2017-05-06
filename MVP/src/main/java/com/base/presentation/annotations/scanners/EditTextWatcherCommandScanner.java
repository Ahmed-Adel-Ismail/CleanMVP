package com.base.presentation.annotations.scanners;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutorFromMethodsGenerator;
import com.base.presentation.annotations.interfaces.EditTextWatcherCommand;

import java.lang.reflect.Method;

/**
 * a {@link ExecutorFromMethodsGenerator} that generates a {@link Executor} instance from
 * {@link EditTextWatcherCommand} annotation
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 *
 * @see ExecutorFromMethodsGenerator
 */
public class EditTextWatcherCommandScanner<Parameter> extends
        ExecutorFromMethodsGenerator<EditTextWatcherCommand, Parameter> {

    /**
     * create a {@link EditTextWatcherCommandScanner}
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    public EditTextWatcherCommandScanner(Executor<Parameter> executor) {
        super(executor, EditTextWatcherCommand.class);
    }

    @Override
    protected void processMethodAnnotation(
            Executor<Parameter> executor,
            final Method method,
            EditTextWatcherCommand annotation) {

        Command<Parameter, ?> command = createCommand(method);
        if (annotation.value().length == 1) {
            executor.put(annotation.value()[0], command);
        } else {
            for (long id : annotation.value()) {
                executor.put(id, command);
            }
        }
    }

}
