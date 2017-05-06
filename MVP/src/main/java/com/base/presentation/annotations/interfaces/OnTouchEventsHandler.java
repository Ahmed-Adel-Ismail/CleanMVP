package com.base.presentation.annotations.interfaces;

import android.view.MotionEvent;

import com.base.abstraction.commands.executors.Executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that indicates the {@link Executor} class that will handle on touch events
 * (the touch events of the Activity, not the on touch listeners) ... naming convention is
 * following Android original naming conventions for the touch events
 * <p>
 * Created by Ahmed Adel on 1/5/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnTouchEventsHandler {

    Class<? extends Executor<MotionEvent>>[] value();
}
