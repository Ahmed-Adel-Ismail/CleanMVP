package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.actors.base.Actor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * determine the address of this {@link Actor}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Address {

    int NULL_VALUE = 0;

    String NULL_NAME = "";

    long value() default NULL_VALUE;

    String name() default NULL_NAME;

}
