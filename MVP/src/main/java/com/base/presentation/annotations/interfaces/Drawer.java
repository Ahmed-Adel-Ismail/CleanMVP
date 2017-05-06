package com.base.presentation.annotations.interfaces;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;

import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * an annotation to indicate that an {@link AbstractActivity} will have a {@link DrawerLayout}
 * with the declared resource ids
 * <p>
 * Created by Ahmed Adel on 12/18/2016.
 */
public @interface Drawer {

    int NULL = 0;

    @IdRes long value();

    @StringRes long openDescRes() default NULL;

    @StringRes long closedDescRes() default NULL;

    @DrawableRes long menuIcon() default NULL;


}
