package com.base.abstraction.commands.executors.generators;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.reflections.FieldInitializer;
import com.base.abstraction.references.CheckedReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * create an {@link Executor} from a field
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 *
 * @param <T>         the type of the annotation that will determine the methods to be triggered
 * @param <Parameter> the type of the parameter for those methods
 */
public class ExecutorFromFieldsGenerator<T extends Annotation, Parameter>
        implements Command<Object, Executor<Parameter>> {

    private Class<T> annotationClass;
    private Executor<Parameter> executor;
    private FieldInitializer<Executor<Parameter>> fieldInitializer;
    private CheckedReference<Object> clientObjectRef;

    /**
     * create a {@link ExecutorFromFieldsGenerator} instance
     *
     * @param executor        the {@link Executor} that will hold the new Commands
     * @param annotationClass the class of the {@link Annotation}
     */
    public ExecutorFromFieldsGenerator(Executor<Parameter> executor, Class<T> annotationClass) {
        this.annotationClass = annotationClass;
        this.executor = executor;
    }

    @NonNull
    @Override
    public Executor<Parameter> execute(Object annotatedObject) {
        this.clientObjectRef = new CheckedReference<>(annotatedObject);
        this.fieldInitializer = new FieldInitializer<>(annotatedObject);
        createFieldAnnotationReader().execute(annotatedObject);
        return executor;
    }

    @NonNull
    private FieldAnnotationScanner<T> createFieldAnnotationReader() {
        return new FieldAnnotationScanner<T>(annotationClass) {
            @Override
            protected void onAnnotationFound(Field element, T annotation) {
                processFieldAnnotation(executor, element, annotation);
            }
        };
    }

    protected final Object getClientObject() throws CheckedReferenceClearedException {
        return clientObjectRef.get();
    }

    /**
     * this method is invoked in
     * {@link FieldAnnotationReader#processAnnotation(Field, Annotation)},
     * but used to add your new {@link Command} to the passed
     * {@link Executor}
     *
     * @param executor   the {@link Executor} that will contain the {@link Command} objects
     * @param field      the {@link Field} that holds the annotation
     * @param annotation the {@link Annotation} that is searched for
     */
    protected void processFieldAnnotation(
            Executor<Parameter> executor,
            Field field,
            T annotation) {
        executor.putAll(fieldInitializer.execute(field));
    }
}
