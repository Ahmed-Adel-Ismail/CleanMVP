package com.base.presentation.interfaces;

import android.app.Activity;

import com.base.presentation.R;

/**
 * an interface implemented by Any Class that can be destroyed while still has a live instance
 * in memory, like for {@link android.app.Activity} instance that enters the
 * {@link Activity#onDestroy()} method ... it is still alive but it is a {@code Destroyed}
 * Object
 * <p>
 * Created by Ahmed Adel on 11/22/2016.
 */
public interface Destroyable {

    /**
     * this method is invoked in {@link R.id#onDestroy} event
     */
    void onDestroy();

}
