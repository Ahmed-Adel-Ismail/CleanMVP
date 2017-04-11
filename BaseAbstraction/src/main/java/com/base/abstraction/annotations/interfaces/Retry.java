package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that indicates that a field should attempt to retry a certain action
 * based on an interval set to the {@link #value()}, the default value is
 * {@link #DEFAULT_INTERVAL_MILLIS}
 * <p>
 * Created by Ahmed Adel on 2/7/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Retry {

    /**
     * the short interval value in milliseconds, it is 3 second delay between retry attempts
     */
    long SHORT_INTERVAL_MILLIS = 3000;

    /**
     * the default interval value in milliseconds, it is 5 seconds delay between retry attempts
     */
    long DEFAULT_INTERVAL_MILLIS = 5000;

    /**
     * the long interval value in milliseconds, it is 7 seconds delay between retry attempts
     */
    long LONG_INTERVAL_MILLIS = 7000;

    long value() default DEFAULT_INTERVAL_MILLIS;

}
