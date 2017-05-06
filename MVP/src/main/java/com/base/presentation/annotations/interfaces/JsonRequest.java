package com.base.presentation.annotations.interfaces;

import com.base.abstraction.commands.Command;
import com.base.presentation.references.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation is used in {@link com.base.presentation.repos.base.Repository} member variables,
 * you should annotate the member variables that will be retrieved
 * in a JSON request to the Class mentioned in the {@link Requester}
 * <p>
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonRequest {

    /**
     * the server request id
     *
     * @return the server request id
     */
    long value();


    /**
     * if the request id will be requested in a concurrent manner or it is easy to do the same
     * request multiple times in a very short amount of time (like a second or less), set this
     * flag to {@code true}
     *
     * @return weather this server request is accepted to be requested in a concurrent manner or
     * not
     */
    boolean concurrent() default false;

    /**
     * the {@link Command} class that will be used to parse the error response
     *
     * @return the {@link Command} that will parse error response
     */
    Class<? extends Command<String, ?>> errorParser();

}
