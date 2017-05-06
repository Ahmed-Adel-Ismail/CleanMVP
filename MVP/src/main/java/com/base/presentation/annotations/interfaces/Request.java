package com.base.presentation.annotations.interfaces;

import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.presentation.repos.base.NullRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation is used in {@link Model} member variables, you should annotate the
 * {@link Entity} member variables with this annotation to be requested from a repository
 * <p>
 * in the {@link com.base.presentation.repos.base.Repository}, you should annotate the member variables that will be retrieved
 * in a request to the Class mentioned in the {@link Requester}
 * <p>
 * to specify a {@link com.base.presentation.repos.base.Repository} to be used by this {@link Entity} other than
 * the default one, you can use {@link #repository()} value
 *
 * Created by Ahmed Adel Ismail on 4/19/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Request {

    /**
     * the server request id
     *
     * @return the server request id
     */
    long value();

    /**
     * this is
     * an optional value to indicate that this {@link Entity} will be requested from a custom
     * repository rather than the one mentioned in {@link Repository}
     *
     * @return the repository other than the one mentioned in {@link Repository}
     */
    Class<? extends com.base.presentation.repos.base.Repository> repository() default NullRepository.class;

}
