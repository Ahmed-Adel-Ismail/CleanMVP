package com.base.presentation.annotations.interfaces;

import android.view.View;

import com.base.abstraction.commands.executors.Executor;

/**
 * an annotation that indicates the {@link Executor} class that will handle clicks
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 */
public @interface OnClickHandler {

    Class<? extends Executor<View>>[] value();
}
