package com.entities.requesters;

import com.base.annotations.Authorization;
import com.base.auth.AuthEntity;
import com.base.auth.AuthGrantTypePassword;
import com.entities.auth.AuthClientDriver;

/**
 * a class that creates the parameters for login action
 * <p>
 * Created by Ahmed Adel on 12/1/2016.
 */
@Authorization(client = AuthClientDriver.class, grantType = AuthGrantTypePassword.class)
public class LoginRequest extends AuthEntity {

    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
