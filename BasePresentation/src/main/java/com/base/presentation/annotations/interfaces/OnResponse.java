package com.base.presentation.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark a method that should not be executed before a certain responses are received
 * <p>
 * Created by Ahmed Adel on 1/30/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnResponse {

    /**
     * the request ids that should be received successfully
     *
     * @return the request ids to be successfully received before invoking the annotated method
     */
    long[] value();

}
