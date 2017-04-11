package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the non-private member variables that indicates an
 * {@link com.base.abstraction.commands.executors.Executor} Object
 * <p>
 * Created by Ahmed Adel on 11/28/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Executor {
}
