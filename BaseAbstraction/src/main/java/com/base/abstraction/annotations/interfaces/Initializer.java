package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the initializer methods with this annotation, in some implementation, an ID is required
 * to identify this initializer, so you can set this ID in the {@link #value()} attribute
 * (which is the default attribute set when you give this annotation a value between brackets)
 * <p>
 * Created by Ahmed Adel on 12/30/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Initializer {

    long[] value() default 0;

}
