package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare your main execute method with this annotation, this will detect the method to execute
 * when the given id occur
 * <p>
 * optionally you can mention the resource id name as a {@code String} value and it will be
 * converted to an id ... notice that you MUST supply one of both values, either the
 * {@link #value()} or the {@link #name()} of the executable
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Executable {

    String NULL_NAME = "";
    long NULL_VALUE = 0;

    long[] value() default NULL_VALUE;

    String name() default NULL_NAME;

}
