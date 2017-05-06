package com.base.usecases.annotations;

import com.base.entities.annotations.MockEntity;
import com.base.usecases.requesters.server.mocks.MockRequester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * declare that an instance variable will be mocked and returned
 * as a response from the {@link MockRequester}, this
 * annotation will result in create an empty Object to be used, if you want a certain mocked
 * response, you can use {@link MockEntity}
 * <p>
 * you can set the response to failure by changing the value of {@link #statusCode()} to what
 * ever response code, so it will be returned in the response, the default is {@code 200}
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mock {

    long value();

    int statusCode() default 200;
}
