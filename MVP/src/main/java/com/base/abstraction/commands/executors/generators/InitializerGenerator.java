package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;

import java.lang.reflect.Method;

/**
 * Created by ESC on 12/30/2016.
 */

public class InitializerGenerator<Parameter> extends
        ExecutorFromMethodsGenerator<Initializer, Parameter> {

    /**
     * the client Object
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    public InitializerGenerator(Executor<Parameter> executor) {
        super(executor, Initializer.class);

    }

    @Override
    protected void processMethodAnnotation(
            Executor<Parameter> executor,
            Method method,
            Initializer annotation) {
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
