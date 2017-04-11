package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.commands.executors.Executor;

/**
 * a {@link ExecutorFromFieldsGenerator} that scans the {@link com.base.abstraction.annotations.interfaces.Executor}
 * annotation and adds those fields to th given {@link Executor} instance
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 *
 * @see ExecutorFromFieldsGenerator
 */
public class ExecutorGenerator<Parameter>
        extends ExecutorFromFieldsGenerator<com.base.abstraction.annotations.interfaces.Executor, Parameter> {

    /**
     * create a {@link ExecutorFromFieldsGenerator} instance
     *
     * @param executor        the {@link Executor} that will hold the new Commands
     */
    public ExecutorGenerator(
            Executor<Parameter> executor) {
        super(executor, com.base.abstraction.annotations.interfaces.Executor.class);
    }
}
