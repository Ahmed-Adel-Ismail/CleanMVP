package com.base.annotations;

import com.base.auth.AuthClient;
import com.base.auth.AuthEntity;
import com.base.auth.AuthGrantType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the authorization details foe a {@link AuthEntity} class
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Authorization {

    Class<? extends AuthClient> client();

    Class<? extends AuthGrantType> grantType();


}
