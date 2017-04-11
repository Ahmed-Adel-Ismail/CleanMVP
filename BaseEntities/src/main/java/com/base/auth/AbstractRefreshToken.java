package com.base.auth;

import com.base.annotations.Authorization;

/**
 * a class that represents the Refresh Auth-token post request entity Object, you will
 * need to extend this class and provide the proper {@link Authorization} values
 * <p>
 * Created by Ahmed Adel on 10/23/2016.
 *
 * @see Authorization
 */
public abstract class AbstractRefreshToken extends AuthEntity {

    private String refresh_token;

    public AbstractRefreshToken() {
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

}
