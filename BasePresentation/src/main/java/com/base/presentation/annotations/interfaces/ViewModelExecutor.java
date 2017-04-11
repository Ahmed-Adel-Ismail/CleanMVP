package com.base.presentation.annotations.interfaces;


import com.base.presentation.base.presentation.ViewModelInvalidationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the {@link ViewModelInvalidationHandler} members in a
 * {@link com.base.presentation.base.presentation.ViewModel} instance with this annotation
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewModelExecutor {

}
