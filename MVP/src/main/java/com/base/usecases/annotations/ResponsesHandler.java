package com.base.usecases.annotations;

import com.base.abstraction.commands.executors.Executor;
import com.base.usecases.events.ResponseMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * specify the {@link Executor} that handles the {@link ResponseMessage}
 * <p>
 * Created by Ahmed Adel on 11/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ResponsesHandler {

    Class<? extends Executor<ResponseMessage>>[] value();
}
