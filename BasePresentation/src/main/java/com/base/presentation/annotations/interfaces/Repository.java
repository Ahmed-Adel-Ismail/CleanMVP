package com.base.presentation.annotations.interfaces;

import com.base.presentation.models.Model;
import com.base.usecases.requesters.base.EntityGateway;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the {@link Model} class with this annotation to declare the related repositories
 * for handling the default {@link EntityGateway} instances to retrieve entities
 * <p>
 * Created by Ahmed Adel on 11/23/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {

    Class<? extends com.base.presentation.repos.base.Repository> value();
}
