package com.base.entities.auth;

/**
 * the {@link AuthGrantType} that represents a password grant type
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public class AuthGrantTypePassword implements AuthGrantType {

    @Override
    public String getValue() {
        return "password";
    }
}
