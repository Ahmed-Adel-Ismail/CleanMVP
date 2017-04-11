package com.base.presentation.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * provide the xml layout that will be the content view, from the resources file, or
 * you can provide it's name through {@link #name()}
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 *
 * @see #value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindLayout {

    int NULL_VALUE = 0;

    String NULL_NAME = "";

    /**
     * get the content view id
     *
     * @return the xml layout from resources
     */
    int value() default NULL_VALUE;

    String name() default NULL_NAME;

}
