package com.base.abstraction.annotations.scanners;

import com.base.abstraction.annotations.readers.MethodAnnotationReader;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * same as {@link MethodAnnotationReader}, but it has an abstract method
 * {@link #onAnnotationFound(Method, Annotation)} that will be invoked if the annotation was
 * found, and if it was not found, {@link AnnotationNotDeclaredException} will <b>NOT</b>
 * be thrown
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
public abstract class MethodAnnotationScanner<T extends Annotation>
        extends MultipleAnnotationsScanner<T, Method> {

    /**
     * create a {@link MethodAnnotationScanner}
     *
     * @param annotationClass the desired {@link Annotation} sub-class
     */
    public MethodAnnotationScanner(Class<T> annotationClass) {
        super(annotationClass);
    }

    @Override
    public Void execute(Object annotatedObject) {
        try {
            MethodAnnotationReader<T> reader = new MethodAnnotationReader<T>(annotationClass) {
                @Override
                protected void processAnnotation(Method method, T annotation) {
                    method.setAccessible(true);
                    onAnnotationFound(method, annotation);
                }
            };
            reader.execute(annotatedObject);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        }
        return null;
    }
}
