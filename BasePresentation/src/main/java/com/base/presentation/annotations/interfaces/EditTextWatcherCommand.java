package com.base.presentation.annotations.interfaces;

import android.support.annotation.IdRes;

import com.base.abstraction.commands.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * add this annotation to methods that will be invoked when an edit text with the given id
 * has changed ... those methods should return a {@link Command}
 * object
 * <p>
 * Created by Ahmed Adel on 11/27/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EditTextWatcherCommand {

    @IdRes long[] value();

}
