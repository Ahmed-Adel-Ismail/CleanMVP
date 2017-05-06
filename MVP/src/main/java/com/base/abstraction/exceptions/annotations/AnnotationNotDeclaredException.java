package com.base.abstraction.exceptions.annotations;

/**
 * an Exception thrown when an annotation was expected to be declared but it is not
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
public class AnnotationNotDeclaredException extends RuntimeException {


    public AnnotationNotDeclaredException(Class<?> annotationClass) {
        super(String.valueOf(annotationClass.getSimpleName()));
    }

}
