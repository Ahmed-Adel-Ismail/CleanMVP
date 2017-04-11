package com.base.presentation.annotations.interfaces;

import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;
import com.base.presentation.repos.base.NullRepository;

/**
 * this annotation is used in both {@link Model} and
 * {@link com.base.presentation.repos.base.Repository} member variables, for {@link Model} class, you should annotate the
 * {@link Entity} member variables with this annotation to be requested from a repository
 * <p>
 * in the {@link com.base.presentation.repos.base.Repository}, you should annotate the member variables that will be retrieved
 * in a request to the Class mentioned in the {@link Requester}
 * <p>
 * to specify a {@link com.base.presentation.repos.base.Repository} to be used by this {@link Entity} other than
 * the default one, you can use {@link #repository()} value
 * <p>
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 */
public @interface JsonRequest {

    /**
     * the server request id
     *
     * @return the server request id
     */
    long value();

    /**
     * to be used in {@link Model} related fields, this is
     * an optional value to indicate that this {@link Entity} will be requested from a custom
     * repository rather than the one mentioned in {@link Repository}
     *
     * @return the repository other than the one mentioned in {@link Repository}
     */
    Class<? extends com.base.presentation.repos.base.Repository> repository() default NullRepository.class;

    /**
     * to be used in {@link com.base.presentation.repos.base.Repository} related fields,
     * if the request id will be requested in a concurrent manner or it is easy to do the same
     * request multiple times in a very short amount of time (like a second or less), set this
     * flag to {@code true}
     *
     * @return weather this server request is accepted to be requested in a concurrent manner or
     * not
     */
    boolean concurrent() default false;

}
