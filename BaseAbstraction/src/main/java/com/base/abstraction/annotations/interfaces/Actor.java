package com.base.abstraction.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the {@link com.base.abstraction.actors.base.Actor Actor}
 * class that will be living along the lifecycle of the application
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Actor {

}
