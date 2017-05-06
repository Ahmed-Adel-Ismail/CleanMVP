package com.base.abstraction.annotations.readers;


import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * a class that reads Annotations and process them if required ... you can override
 * {@link #processAnnotation(Method, Annotation)} to do your code there,
 * and optionally, you can override {@link #isMethodAccepted(Method, Annotation)} if you want
 * to add extra validation to the annotated methods
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 *
 * @see <a href="http://stackoverflow.com/a/6593661/1956013">source</a>
 */

public class MethodAnnotationReader<T extends Annotation> implements
        Command<Object, Map<Method, T>> {

    private Class<T> annotationClass;

    public MethodAnnotationReader(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public Map<Method, T> execute(Object objectWithAnnotatedMethods) {
        Map<Method, T> methods = new HashMap<>();
        Class<?> klass = objectWithAnnotatedMethods.getClass();
        while (klass != Object.class) {
            klass = scanMethodsAndGetParentClass(methods, klass);
        }
        if (methods.isEmpty()) {
            throw new AnnotationNotDeclaredException(annotationClass);
        }
        return methods;
    }

    private Class<?> scanMethodsAndGetParentClass(Map<Method, T> methods, Class<?> klass) {
        Method[] allMethods = klass.getDeclaredMethods();
        if (allMethods != null) {
            for (Method method : allMethods) {
                scanMethodsForThisClass(methods, method);
            }
        }
        klass = klass.getSuperclass();
        return klass;
    }

    private void scanMethodsForThisClass(Map<Method, T> methods, Method method) {
        if (method.isAnnotationPresent(annotationClass)) {
            T annotation = method.getAnnotation(annotationClass);
            if (isMethodAccepted(method, annotation)) {
                methods.put(method, annotation);
                processAnnotation(method, annotation);
            }
        }
    }


    /**
     * an optional method invoked every time an annotated method is <b>Accepted</b>,
     * this method is invoked <b>ONLY IF</b> {@link #isMethodAccepted(Method, Annotation)}
     * returned {@code true}
     *
     * @param method     the annotated method
     * @param annotation the {@link Annotation} instance
     * @return {@code true} if this method is accepted for being added to the
     * resulting map, else return {@code false} to ignore it ... the default return value is
     * {@code true}
     */
    protected boolean isMethodAccepted(Method method, T annotation) {
        // template method
        return true;
    }

    /**
     * an optional method invoked every time an annotated method is detected
     *
     * @param method     the annotated method
     * @param annotation the {@link Annotation} instance
     */
    protected void processAnnotation(Method method, T annotation) {
        // template method
    }


}


