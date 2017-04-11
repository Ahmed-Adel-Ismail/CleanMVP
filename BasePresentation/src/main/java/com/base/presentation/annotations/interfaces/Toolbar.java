package com.base.presentation.annotations.interfaces;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.ViewBinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the custom {@link android.support.v7.widget.Toolbar} id, this annotation is ignored if
 * the hosting screen for the {@link ViewBinder} is not an {@link AbstractActivity}
 * <p>
 * Created by Ahmed Adel on 12/15/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Toolbar {

    int NULL = 0;

    @IdRes long value();

    @DrawableRes long logo() default NULL;

    boolean showTitle() default false;

}
