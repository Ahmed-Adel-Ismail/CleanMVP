package com.base.presentation.base.abstracts.features;

import android.os.Bundle;

/**
 * an interface that should be implemented by {@link android.app.Activity} or
 * {@link android.app.Fragment}
 * Created by Ahmed Adel on 9/4/2016.
 */
interface ViewBindable {

    /**
     * create the {@link ViewBinder} that will hold the true implementation for the
     * Activity or Fragment, this method is invoked before calling
     * {@link android.app.Activity#setContentView(int)}
     *
     * @param savedInstanceState the parameter passed to
     *                           {@link android.app.Activity#onCreate(Bundle)}
     * @return the {@link ViewBinder} that will hold the true implementation for
     * this Activity or Fragment
     */
    ViewBinder createViewBinder(Bundle savedInstanceState);
}
