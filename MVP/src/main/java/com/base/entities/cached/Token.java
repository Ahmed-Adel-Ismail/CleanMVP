package com.base.entities.cached;

import com.base.entities.annotations.MockEntity;

import java.io.Serializable;

/**
 * a class representing the Token for accessing the server
 * <p>
 * Created by Wafaa on 6/5/2016.
 */
@MockEntity
public class Token implements Serializable {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

}
