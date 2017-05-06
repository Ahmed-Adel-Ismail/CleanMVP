package com.base.abstraction.commands.executors;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.scanners.ClassAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.generators.ExecutableCommandsGenerator;
import com.base.abstraction.commands.executors.generators.ExecutableGenerator;
import com.base.abstraction.commands.executors.generators.ExecutorGenerator;

/**
 * a sub-class for the {@link CommandExecutor} that works with {@code Long} as the keys,
 * subclasses of this class can declare there {@code non-private} methods with
 * {@link Executable} or {@link ExecutableCommand} and provide the ids to be handled in the
 * annotation's value ... which will invoke this method in a {@link Command} that is mapped to
 * the given id(s)
 * <p>
 * to add another instance to this one / invoke {@link #putAll(CommandExecutor)}, you will need
 * to declare the other instance as a <b>non-private</b> member variable, and annotate it with
 * {@link com.base.abstraction.annotations.interfaces.Executor} ... the other instance should use the same
 * generic type of the current
 * <p>
 * other executors are added first, before the current executor methods, so if both
 * has a method that is invoked on the same ID
 * <p>
 * to make this instance read the declared annotations in it, you should annotate this instance
 * with {@link Load}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 *
 * @param <Parameter> the type of the Object that is passed to the methods invoked when
 *                    executing the methods mapped to the desired id
 * @see com.base.abstraction.annotations.interfaces.Executor
 * @see Executable
 * @see ExecutableCommand
 * @see Load
 */
public class Executor<Parameter> extends CommandExecutor<Long, Parameter> {

    public Executor() {
        new ClassAnnotationScanner<Load>(Load.class) {
            @Override
            protected void onAnnotationFound(Load annotation) {
                scanAllAnnotations();
            }
        }.execute(this);
    }

    private void scanAllAnnotations() {
        new ExecutableGenerator<>(this).execute(this);
        new ExecutableCommandsGenerator<>(this).execute(this);
        new ExecutorGenerator<>(this).execute(this);
    }


}
