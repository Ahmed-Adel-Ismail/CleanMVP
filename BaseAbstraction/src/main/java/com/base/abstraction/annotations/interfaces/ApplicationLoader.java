package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the class that will be responsible for loading data for application, this class
 * will be checked in every screen, if it did not load the data required for this screen,
 * a Loader UI will be displayed instead of the normal UI
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationLoader {

    Class<? extends com.base.abstraction.system.AppLoader> value();

}
