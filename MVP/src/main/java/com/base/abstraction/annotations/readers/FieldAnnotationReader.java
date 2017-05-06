package com.base.abstraction.annotations.readers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * a class that reads Annotations and process them if required ... you can override
 * {@link #processAnnotation(Field, Annotation)} to do your code there,
 * and optionally, you can override {@link #isFieldAccepted(Field, Annotation)}  if you want
 * to add extra validation to the annotated fields
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 */

public class FieldAnnotationReader<T extends Annotation> implements Command<Object, Map<Field, T>> {

    private Class<T> annotationClass;

    public FieldAnnotationReader(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public Map<Field, T> execute(Object objectWithAnnotatedFields) {
        Map<Field, T> fields = new HashMap<>();
        Class<?> klass = objectWithAnnotatedFields.getClass();
        while (klass != Object.class) {
            klass = scanFieldsAndGetParentClass(fields, klass);
        }
        if (fields.isEmpty()) {
            throw new AnnotationNotDeclaredException(annotationClass);
        }
        return fields;
    }

    private Class<?> scanFieldsAndGetParentClass(Map<Field, T> fields, Class<?> klass) {
        Field[] allFields = klass.getDeclaredFields();
        if (allFields != null) {
            for (Field field : allFields) {
                scanFieldsForThisClass(fields, field);
            }
        }
        klass = klass.getSuperclass();
        return klass;
    }

    private void scanFieldsForThisClass(Map<Field, T> fields, Field field) {
        if (field.isAnnotationPresent(annotationClass)) {
            T annotation = field.getAnnotation(annotationClass);
            if (isFieldAccepted(field, annotation)) {
                fields.put(field, annotation);
                processAnnotation(field, annotation);
            }
        }
    }


    /**
     * an optional method invoked every time an annotated field is <b>Accepted</b>,
     * this method is invoked <b>ONLY IF</b> {@link #isFieldAccepted(Method, Annotation)}
     * returned {@code true}
     *
     * @param field      the annotated field
     * @param annotation the {@link Annotation} instance
     * @return {@code true} if this field is accepted for being added to the
     * resulting map, else return {@code false} to ignore it ... the default return value is
     * {@code true}
     */
    protected boolean isFieldAccepted(Field field, T annotInstance) {
        // template method
        return true;
    }

    /**
     * an optional method invoked every time an annotated field is detected
     *
     * @param field      the annotated field
     * @param annotation the {@link Annotation} instance
     */
    protected void processAnnotation(Field field, T annotation) {
        // template method
    }
}
