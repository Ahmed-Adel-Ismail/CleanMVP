package com.base.abstraction.annotations.interfaces;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare this annotation over Activities that will be opened when a {@link View} is clicked,
 * provide the ids of the views that will open this {@link Activity} in the
 * {@link #value()}
 * <p>
 * Created by Ahmed Adel on 1/2/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StartOnClick {

    long[] value();

}
