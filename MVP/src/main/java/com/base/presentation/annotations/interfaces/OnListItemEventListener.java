package com.base.presentation.annotations.interfaces;

import com.base.abstraction.commands.executors.Executor;
import com.base.presentation.listeners.OnListItemEventListenerParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that indicates the {@link Executor} class that will handle list item clicks
 * <p>
 * Created by Wafaa on 12/27/2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnListItemEventListener {

    Class<? extends Executor<OnListItemEventListenerParams>>[] value();
}
