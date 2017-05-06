package com.base.presentation.base.abstracts.features;

import android.app.Activity;
import android.os.Bundle;

/**
 * implement this interface by your class that extends the {@link ViewBinder} class but it will
 * also be the parent of another {@link ViewBinder} classes
 * <p>
 * Created by Ahmed Adel on 11/21/2016.
 */
public interface ViewBindersParent {

    /**
     * same as {@link #initializeViews(Bundle)} but after calling the common UI related
     * calls like {@link ButterKnife#bind(Object, Activity)} for example
     *
     * @param savedInstanceState same as {@link #initializeViews(Bundle)}
     */
    void initializeAfterBindingViews(Bundle savedInstanceState);
}
