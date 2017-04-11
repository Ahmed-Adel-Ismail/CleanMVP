package com.base.presentation.annotations.interfaces;

import com.base.presentation.base.services.AbstractService;
import com.base.presentation.base.services.ForegroundServiceBuilder;
import com.base.presentation.base.services.NullForegroundServiceBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare this annotation on {@link AbstractService} to make id a foreground service that
 * cannot be killed when the application is not working
 * <p>
 * Created by Ahmed Adel on 1/17/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Foreground {

    Class<? extends ForegroundServiceBuilder> value() default NullForegroundServiceBuilder.class;


}
