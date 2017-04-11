package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.services.AbstractService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark an {@link AbstractService} with this annotation to make it run on the main thread instead
 * of running on a background thread
 * <p>
 * Created by Ahmed Adel on 1/17/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MainThread {
}
