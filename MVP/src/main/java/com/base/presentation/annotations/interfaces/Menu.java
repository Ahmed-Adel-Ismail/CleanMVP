package com.base.presentation.annotations.interfaces;

import android.support.annotation.MenuRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation that declares the menu xml file required for an activity, if there are menu-groups
 * that are visible, you can set there ids in {@link #menuGroups()}
 * <p>
 * Created by Wafaa on 12/12/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Menu {

    @MenuRes long value();

    long[] menuGroups() default {0};
}
