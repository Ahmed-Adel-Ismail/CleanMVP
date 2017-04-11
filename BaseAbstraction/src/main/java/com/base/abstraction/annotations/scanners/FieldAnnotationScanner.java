package com.base.abstraction.annotations.scanners;

import com.base.abstraction.annotations.readers.FieldAnnotationReader;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * same as {@link FieldAnnotationReader}, but it has an abstract method
 * {@link #onAnnotationFound(Field, Annotation)} that will be invoked if the annotation was
 * found, and if it was not found, {@link AnnotationNotDeclaredException} will <b>NOT</b>
 * be thrown
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
public abstract class FieldAnnotationScanner<T extends Annotation>
        extends MultipleAnnotationsScanner<T, Field> {


    /**
     * create a {@link FieldAnnotationScanner}
     *
     * @param annotationClass the desired {@link Annotation} sub-class
     */
    public FieldAnnotationScanner(Class<T> annotationClass) {
        super(annotationClass);
    }

    @Override
    public Void execute(Object annotatedObject) {
        try {
            FieldAnnotationReader<T> reader = new FieldAnnotationReader<T>(annotationClass) {
                @Override
                protected void processAnnotation(Field field, T annotation) {
                    field.setAccessible(true);
                    onAnnotationFound(field, annotation);
                }
            };
            reader.execute(annotatedObject);
        } catch (AnnotationNotDeclaredException e) {
            // do nothing
        }
        return null;
    }


}
