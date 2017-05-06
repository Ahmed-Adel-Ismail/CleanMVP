package com.base.entities.auth;

import com.base.entities.annotations.Authorization;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * the parent class for all classes that will be used as parameters in Authorization URLs,
 * it is mandatory to mention the authorization parameters through {@link Authorization}
 * <p>
 * Created by Ahmed Adel on 11/15/2016.
 *
 * @see {@link Authorization}
 */
public class AuthEntity implements Serializable {

    private String client_id;
    private String client_secret;
    private String grant_type;

    public AuthEntity() {
        Authorization authorization = getClass().getAnnotation(Authorization.class);
        String clientValue = createNewInstance(authorization.client()).getValue();
        client_id = clientValue;
        client_secret = clientValue;
        grant_type = createNewInstance(authorization.grantType()).getValue();
    }

    private <C> C createNewInstance(Class<? extends C> klass) {
        try {
            Constructor<? extends C> constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Throwable e) {
            throw new UnsupportedOperationException("could not initialize Authorization " +
                    "related class : " + e.getMessage());
        }

    }


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

}
