package com.base.presentation.annotations.interfaces;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * add this annotation to methods that will be invoked when an edit text with the given id
 * has changed
 * object
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EditTextWatcher {

    @IdRes long[] value();

}
