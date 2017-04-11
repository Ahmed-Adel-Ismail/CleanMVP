package com.base.usecases.annotations;

import com.base.abstraction.api.usecases.AbstractIntegrationHandler;
import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.system.App;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the web-socket URL locator for the project, it should be a secure / wss url locator,
 * although {@link #value()} has a default value, it is not optional when it is declared on
 * {@link App} class
 * <p>
 * for classes that use {@link RequestUrlLocator}, they need to declare this annotation to
 * make {@link AbstractIntegrationHandler} get the Wss locator for them
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebSocketUrlLocator {

    Class<? extends RequestUrlLocator> value() default NullUrlLocator.class;
}
