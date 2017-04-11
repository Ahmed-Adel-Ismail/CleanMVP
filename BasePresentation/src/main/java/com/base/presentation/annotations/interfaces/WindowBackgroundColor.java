package com.base.presentation.annotations.interfaces;

import android.graphics.Color;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * determine the background color for the Activity window
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 *
 * @see #value()
 * @deprecated not used yet ...
 * {@link com.base.presentation.base.abstracts.features.ActivityThemeDrawer} need to be fixed
 * first
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WindowBackgroundColor {

    /**
     * the window background color, like {@link Color#TRANSPARENT} by default
     *
     * @return the window background color
     */
    int value();

}
