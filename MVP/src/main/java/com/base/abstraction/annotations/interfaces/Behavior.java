package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.system.Behaviors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the current behavior of the application, from {@link com.base.abstraction.system.Behaviors}
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Behavior {

    Behaviors value();

}
