package com.base.presentation.annotations.scanners;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutorFromMethodsGenerator;
import com.base.presentation.annotations.interfaces.EditTextWatcher;

import java.lang.reflect.Method;

/**
 * a {@link ExecutorFromMethodsGenerator} that generates a {@link Executor} instance from
 * {@link EditTextWatcher} annotation
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 *
 * @see ExecutorFromMethodsGenerator
 */

public class EditTextWatcherScanner<Parameter> extends
        ExecutorFromMethodsGenerator<EditTextWatcher, Parameter> {

    /**
     * create a {@link EditTextWatcherScanner}
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    public EditTextWatcherScanner(Executor<Parameter> executor) {
        super(executor, EditTextWatcher.class);
    }

    @Override
    protected void processMethodAnnotation(
            Executor<Parameter> executor,
            final Method method,
            EditTextWatcher annotation) {

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
