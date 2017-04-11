package com.base.abstraction.commands.executors.generators;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.readers.MethodAnnotationReader;
import com.base.abstraction.annotations.scanners.MethodAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.exceptions.propagated.DuckableException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * create an {@link Executor} that creates it's Commands based on a
 * given {@link Annotation} on methods
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 *
 * @param <T>         the type of the annotation that will determine the methods to be triggered
 * @param <Parameter> the type of the parameter for those methods, and is declared as the
 *                    {@link Executor} type as well
 */
public abstract class ExecutorFromMethodsGenerator<T extends Annotation, Parameter>
        implements Command<Object, Executor<Parameter>> {

    private Class<T> annotationClass;
    private Executor<Parameter> executor;
    private CheckedReference<Object> clientObjectRef;

    /**
     * create a {@link ExecutorFromMethodsGenerator} instance
     *
     * @param executor        the {@link Executor} that will hold the new Commands
     * @param annotationClass the class of the {@link Annotation}
     */
    public ExecutorFromMethodsGenerator(Executor<Parameter> executor, Class<T> annotationClass) {
        this.annotationClass = annotationClass;
        this.executor = executor;
    }

    /**
     * scan the annotated Object for annotations and fill the
     * {@link Executor} passed to the constructor, then return
     * the new {@link Executor}
     *
     * @param annotatedObject the instance to be scanned, if this instance is declared as a
     *                        {@code private} class, the operation will fail
     * @return the {@link Executor} updated with the new values
     */
    @NonNull
    @Override
    public Executor<Parameter> execute(Object annotatedObject) {
        this.clientObjectRef = new CheckedReference<>(annotatedObject);
        createMethodAnnotationReader().execute(annotatedObject);
        return executor;
    }

    @NonNull
    private MethodAnnotationScanner<T> createMethodAnnotationReader() {
        return new MethodAnnotationScanner<T>(annotationClass) {
            @Override
            protected void onAnnotationFound(Method element, T annotation) {
                processMethodAnnotation(executor, element, annotation);
            }
        };
    }

    protected final Object getClientObject() throws CheckedReferenceClearedException {
        return clientObjectRef.get();
    }

    /**
     * this method is invoked in
     * {@link MethodAnnotationReader#processAnnotation(Method, Annotation)},
     * but used to add your new {@link Command} to the passed {@link Executor}
     *
     * @param executor   the {@link Executor} that will contain the {@link Command} objects
     * @param method     the {@link Method} that holds the annotation
     * @param annotation the {@link Annotation} that is searched for
     */
    protected abstract void processMethodAnnotation(
            Executor<Parameter> executor,
            Method method,
            T annotation);


    /**
     * create the {@link Command} object that will execute the given method
     *
     * @param method the {@link Method} to be executed
     * @return the {@link Command} to be used
     */
    @NonNull
    protected final Command<Parameter, ?> createCommand(final Method method) {
        return new Command<Parameter, Void>() {
            @Override
            public Void execute(Parameter param) {
                try {
                    method.setAccessible(true);
                    method.invoke(getClientObject(), param);
                } catch (CheckedReferenceClearedException e) {
                    Logger.getInstance().exception(e);
                } catch (InvocationTargetException e) {
                    return throwOriginalException(e);
                } catch (Throwable e) {
                    new TestException().execute(e);
                }
                return null;
            }

            @NonNull
            private Void throwOriginalException(InvocationTargetException e) {
                Throwable throwable = e.getCause();
                if (throwable instanceof DuckableException) {
                    throw (DuckableException) throwable;
                } else if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new RuntimeException(throwable);
                }
            }
        };
    }
}
