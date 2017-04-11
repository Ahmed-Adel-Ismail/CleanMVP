package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark a class or a field with this annotation to save itself on creation, some classes save them
 * selves from annotations declared on there fields or methods, other classes or fields may save
 * them selves to preferences, every class / field handles it the way it decides
 * <p>
 * you can declare the {@link Source} you want the object to save from in the values
 * of this annotation, the default {@link #source()} is {@link Source#INTENT}
 * <p>
 * Created by Ahmed Adel on 2/5/2017.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Save {

    long[] value() default 0;

    @Source
    int source() default Source.INTENT;
}
