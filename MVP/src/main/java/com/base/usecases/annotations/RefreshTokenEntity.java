package com.base.usecases.annotations;

import com.base.entities.auth.AbstractRefreshToken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the implementer of the {@link AbstractRefreshToken} class
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RefreshTokenEntity {

    Class<? extends AbstractRefreshToken> value();
}
