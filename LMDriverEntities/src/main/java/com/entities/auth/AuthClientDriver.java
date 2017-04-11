package com.entities.auth;

import com.base.auth.AuthClient;

/**
 * the {@link AuthClient} that represents the Driver client
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public class AuthClientDriver implements AuthClient {
    @Override
    public String getValue() {
        return "DRIVER";
    }
}
