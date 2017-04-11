package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.presentation.ValidatorViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * working on {@link ValidatorViewModel}
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 *
 * @deprecated not tested yet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ViewValidationCommand {

    long viewId();

    long errorViewId();

    ValidatorViewModel.Visibility defaultVisibility() default ValidatorViewModel.Visibility.GONE;


}
