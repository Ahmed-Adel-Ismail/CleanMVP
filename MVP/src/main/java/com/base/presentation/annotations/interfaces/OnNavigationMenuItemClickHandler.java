package com.base.presentation.annotations.interfaces;

import android.view.MenuItem;

import com.base.abstraction.commands.executors.Executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that indicates the {@link Executor} class that will handle menu item clicks
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnNavigationMenuItemClickHandler {

    Class<? extends Executor<MenuItem>>[] value();

}
