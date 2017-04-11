package com.base.abstraction.annotations.scanners;

import com.base.abstraction.commands.Command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * same as {@link ClassAnnotationScanner} but for multiple elements, like {@link Field} or
 * {@link java.lang.reflect.Method}
 * <p>
 * Created by Ahmed Adel on 11/30/2016.
 */
abstract class MultipleAnnotationsScanner<T extends Annotation, E>
        implements Command<Object, Void> {


    final Class<T> annotationClass;

    /**
     * create a {@link MultipleAnnotationsScanner}
     *
     * @param annotationClass the desired {@link Annotation} sub-class
     */
    MultipleAnnotationsScanner(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }


    /**
     * invoked only if the desired {@link Annotation} is found
     *
     * @param element    the element that is annotated
     * @param annotation the {@link Annotation} that was searched for
     */
    protected abstract void onAnnotationFound(E element, T annotation);
}
