package com.base.abstraction.annotations.readers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;

/**
 * a class that reads Class Annotations from instances passed
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
public class ClassAnnotationReader<AnnotationInstance extends Annotation>
        implements Command<Object, AnnotationInstance> {

    private Class<? extends AnnotationInstance> annotationClass;

    public ClassAnnotationReader(Class<? extends AnnotationInstance> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public AnnotationInstance execute(Object annotatedObject) {
        Class<?> annotatedClass = annotatedObject.getClass();
        if (annotatedClass.isAnnotationPresent(annotationClass)) {
            return annotatedClass.getAnnotation(annotationClass);
        } else {
            throw new AnnotationNotDeclaredException(annotatedClass);
        }

    }
}
