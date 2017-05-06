package com.base.presentation.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request a window feature for the annotated Activity
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 *
 * @see #value()
 * @deprecated @deprecated not used yet ...
 * {@link com.base.presentation.base.abstracts.features.ActivityThemeDrawer} need to be fixed
 * first
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WindowFeature {

    /**
     * set the window feature for the value Activity, like
     * {@link android.view.Window#FEATURE_NO_TITLE}
     *
     * @return the window feature required
     */
    int value();

}
