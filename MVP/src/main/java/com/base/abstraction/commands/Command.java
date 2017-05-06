package com.base.abstraction.commands;

import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.abstraction.commands.params.Args2;
import com.base.abstraction.commands.params.Args3;
import com.base.abstraction.commands.params.ArgsGen;

/**
 * a Command (or a function) that can be executed at any point of the flow, it has only one method, which is
 * {@link #execute(Object)}
 * <p>
 * for any class that you want to be used as a separate component that does one thing (which is
 * supposed to be the normal behavior for a {@code Single Responsibility} class), it can
 * implement this interface, and be used in a flexible manner all over the application ...
 * this interface is widely used, and it is <i>encouraged</i> to implement this it, to
 * give your classes <b>functional</b> flexibility in a {@code event-driven} architecture
 * like the current one
 * <p>
 * to declare multiple arguments to the {@link #execute(Object)} method, you can use
 * {@link Args2 Dual Arguments} or
 * {@link Args3 Triple Arguments} or related classes,
 * and to generate those instances from clients you can use
 * {@link ArgsGen} class
 * <p>
 * Created by Ahmed Adel on 8/30/2016.
 *
 * @param <Parameter> the parameter type for {@link #execute(Object)}, it can be {@link Void}
 * @param <Return>    the return type for {@link #execute(Object)}, it can be {@link Void}
 * @see CommandExecutor
 */
public interface Command<Parameter, Return> {


    /**
     * the executer method for the {@link Command} Object
     *
     * @param p the parameter to be used
     * @return the type expected based on the Generic parameter passed for {@link Command}
     */
    Return execute(Parameter p);

}
