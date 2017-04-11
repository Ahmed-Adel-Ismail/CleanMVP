package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * provide the {@link Executor} that will handle {@link Message} updates
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UpdatesHandler {
    Class<? extends Executor<Message>>[] value();
}
