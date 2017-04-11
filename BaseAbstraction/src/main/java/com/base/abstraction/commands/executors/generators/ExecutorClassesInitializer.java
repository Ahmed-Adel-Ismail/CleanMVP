package com.base.abstraction.commands.executors.generators;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.references.CheckedReference;

/**
 * an unchecked class that creates an {@link Executor} from an
 * {@link java.lang.annotation.Annotation} that declares its value as an array of {@link Executor}
 * classes, this class does not do any type checking, it expects the {@link Executor} to
 * implement the {@link com.base.abstraction.interfaces.Initializable} interface
 * <p>
 * this class is created due to the duplicate use of the same code snippet all over the
 * classes that reads annotations
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
@SuppressWarnings("unchecked")
public class ExecutorClassesInitializer implements Command<Class<?>[], Executor> {

    private CheckedReference<Object> clientObject;

    public ExecutorClassesInitializer(Object clientObject) {
        this.clientObject = new CheckedReference<>(clientObject);
    }

    @Override
    public Executor execute(Class<?>[] klasses) {
        try {
            return createExecutor(klasses);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
        return null;
    }

    private Executor createExecutor(Class<?>[] classes)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Executor executor = new Executor<>();
        if (classes.length == 1) {
            executor.putAll(createInitializableExecutor(classes[0]));
        } else {
            for (Class<?> clss : classes) {
                executor.putAll(createInitializableExecutor(clss));
            }
        }
        return executor;
    }

    private Executor createInitializableExecutor(Class<?> clss)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Executor executor;
        executor = (Executor) new Initializer<>().execute(clss);
        ((Initializable) executor).initialize(clientObject.get());
        return executor;
    }
}
