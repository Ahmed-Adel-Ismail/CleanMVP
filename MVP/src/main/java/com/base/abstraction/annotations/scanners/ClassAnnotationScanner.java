package com.base.abstraction.annotations.scanners;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;

/**
 * same as {@link ClassAnnotationReader}, but it has an abstract method
 * {@link #onAnnotationFound(Annotation)} that will be invoked if the annotation was
 * found, and if it was not found, {@link AnnotationNotDeclaredException} will <b>NOT</b>
 * be thrown
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
public abstract class ClassAnnotationScanner<T extends Annotation> implements Command<Object, Void> {

    private Class<T> annotationClass;

    /**
     * create a {@link ClassAnnotationScanner}
     *
     * @param annotationClass the desired {@link Annotation} sub-class
     */
    public ClassAnnotationScanner(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public Void execute(Object annotatedObject) {
        try {
            ClassAnnotationReader<T> reader = new ClassAnnotationReader<T>(annotationClass);
            T annotation = reader.execute(annotatedObject);
            onAnnotationFound(annotation);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        }
        return null;
    }

    /**
     * invoked only if the desired {@link Annotation} is found
     *
     * @param annotation the {@link Annotation} that was searched for
     */
    protected abstract void onAnnotationFound(T annotation);

}
