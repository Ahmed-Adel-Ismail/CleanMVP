package com.base.abstraction.annotations.interfaces;

import com.base.abstraction.system.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare a {@link com.base.abstraction.commands.executors.Executor} that will handle global
 * broadcasts per application (Events produced by the {@link EventBus}, it's methods should be annotated with {@link android.R.string}
 * constant values as these {@code String} resources will be the action of the broadcasts expected.
 * <p>
 * every method will declare the {@code String} resource id in it's {@link Executable} annotation,
 * these {@code String} resources are the Actions of the broadcasts, when any broadcast holds the
 * action of the mentioned {@code String} resource, this method will be executed
 * <p>
 * remember to annotate your {@link com.base.abstraction.commands.executors.Executor} with
 * {@link Load} annotation for it to read all the methods decalred in it
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EventBusSubscriber {


}
