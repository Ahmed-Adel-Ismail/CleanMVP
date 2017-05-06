package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.abstracts.features.AbstractActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that should be added to
 * {@link AbstractActivity} sub-classes that will display as the splash screen
 * <p>
 * Created by Ahmed Adel on 9/7/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Splash {
}
