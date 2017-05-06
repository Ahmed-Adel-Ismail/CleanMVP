package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare your main execute method with this annotation, this will detect the method to execute
 * when the given id occur ... this annotation is used to declare a {@link Command} to be
 * added directly to an {@link Executor}, unlike {@link Executable} which is used to
 * mark normal methods that will be put in a {@link Command} by the {@link Executor} itself
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExecutableCommand {

    long[] value();

}
