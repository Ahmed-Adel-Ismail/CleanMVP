package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.abstracts.features.ViewBinder;
import com.base.presentation.base.loaders.AbstractLoaderViewBinder;
import com.base.presentation.base.loaders.NullLoaderViewBinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * determine the {@link ViewBinder View Binders} for an  {@link AbstractActivity}
 * <p>
 * you will need to set the {@link #value()} with the normal {@link ViewBinder}, and
 * you will need to provide the {@link AbstractLoaderViewBinder} sub-class in the
 * {@link #error()} value ... where the {@link #error()} will be used in case that the
 * {@link AbstractActivity} could not be loaded due to an error
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityViewBinders {

    Class<? extends ViewBinder> value();

    Class<? extends AbstractLoaderViewBinder> error() default NullLoaderViewBinder.class;
}