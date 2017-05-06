package com.base.usecases.annotations;

import com.base.abstraction.commands.executors.Executor;
import com.base.usecases.events.RequestMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * specify the {@link Executor} that will handle {@link RequestMessage}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestsHandler {

    Class<? extends Executor<RequestMessage>>[] value();

}
