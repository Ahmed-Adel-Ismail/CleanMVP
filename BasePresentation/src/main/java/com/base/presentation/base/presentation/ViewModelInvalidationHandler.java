package com.base.presentation.base.presentation;


import android.view.View;

import com.base.abstraction.commands.executors.Executor;

/**
 * the {@link Executor} for the {@link ViewModel} class, you can access the {@link ViewModel}
 * through {@link #getViewModel()} in your sub-classes
 * <p>
 * Created by Ahmed Adel on 11/28/2016.
 */
public class ViewModelInvalidationHandler<V extends ViewModel> extends ViewModelHandler<View, V> {


}
