package com.base.entities.annotations;

import com.base.entities.cached.ServerMessage;
import com.base.entities.mocked.MockedFailureServerMessage;
import com.base.entities.cached.Null;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate any {@link Serializable} with this annotation to provide value and failure mocked Objects
 * for it, the default mocks are {@link ServerMessage} Objects
 * <p>
 * you will need to supply an {@link Serializable} for {@link #value()}, if not, an
 * empty instance will be generated as the mocked Object, and optionally,
 * you can provide an {@link Serializable} for {@link #error()} ... which is
 * {@link MockedFailureServerMessage} by default
 * <p>
 * Created by Ahmed Adel on 12/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MockEntity {

    Class<? extends Serializable> value() default Null.class;

    Class<? extends Serializable> error() default MockedFailureServerMessage.class;

}
