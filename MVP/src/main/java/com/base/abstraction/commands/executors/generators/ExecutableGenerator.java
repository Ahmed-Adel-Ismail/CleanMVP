package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.system.AppResources;

import java.lang.reflect.Method;

/**
 * a class that reads the annotations of {@link Executable} in an instance and adds all these
 * methods in a {@link Executor} Object
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 *
 * @see ExecutorFromMethodsGenerator
 */
public class ExecutableGenerator<Parameter> extends ExecutorFromMethodsGenerator<Executable, Parameter> {

    /**
     * the client Object
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    public ExecutableGenerator(Executor<Parameter> executor) {
        super(executor, Executable.class);

    }

    @Override
    protected void processMethodAnnotation(
            Executor<Parameter> executor,
            Method method,
            Executable annotation) {

        Command<Parameter, ?> command = createCommand(method);
        if (annotation.value().length == 1) {
            executor.put(readExecutableId(annotation), command);
        } else {
            for (long id : annotation.value()) {
                executor.put(id, command);
            }
        }


    }

    private long readExecutableId(Executable annotation) {
        long id = annotation.value()[0];
        if (id == Executable.NULL_VALUE) {
            id = readResourceIdByName(annotation);
        }
        return id;
    }

    private long readResourceIdByName(Executable annotation) {
        if (Executable.NULL_NAME.equals(annotation.name())) {
            throw new UnsupportedOperationException("must supply a value or a name @"
                    + Executable.class.getSimpleName());
        }
        return AppResources.id(annotation.name());
    }


}
