package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.api.usecases.AbstractIntegrationHandler;
import com.base.abstraction.api.usecases.RequestUrlLocator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the Use cases API for the project, the class which provides details for the base classes
 * to use it, like {@link RequestUrlLocator} implementers for example
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Integration {

    Class<? extends AbstractIntegrationHandler> value();
}
