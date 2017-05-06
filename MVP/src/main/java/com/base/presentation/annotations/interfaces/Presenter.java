package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.abstracts.features.ViewBinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the {@link com.base.presentation.base.presentation.Presenter} member variables of the
 * {@link ViewBinder} with this annotation to let the {@link ViewBinder} add them to itself
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Presenter {
}
