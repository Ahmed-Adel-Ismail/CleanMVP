package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.system.App;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare this annotation for the {@link App} class to know the class that holds the application
 * flow ... this flow class should hold all the Activities, fragments, so it can reach for
 * those components and control them
 * <p>
 * Created by Ahmed Adel on 1/2/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationFlow {

    Class<?> value();

}
