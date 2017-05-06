package com.base.entities.auth;

/**
 * the {@link AuthGrantType} that represents a refresh token grant type
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public class AuthGrantTypeRefreshToken implements AuthGrantType {
    @Override
    public String getValue() {
        return "refresh_token";
    }
}
