package com.entities.auth;

import com.base.auth.AuthClient;

/**
 * the {@link AuthClient} that represents the Android client
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public class AuthClientAndroid implements AuthClient {

    @Override
    public String getValue() {
        return "ANDROID";
    }
}
