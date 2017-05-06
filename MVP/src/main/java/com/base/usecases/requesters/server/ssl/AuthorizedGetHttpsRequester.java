package com.base.usecases.requesters.server.ssl;

import com.base.usecases.requesters.server.base.HttpMethod;

/**
 * a {@link AuthorizedHttpsRequester} but uses {@link HttpMethod#GET} instead of
 * {@link HttpMethod#POST}
 * <p>
 * Created by Ahmed Adel on 1/4/2017.
 */
public class AuthorizedGetHttpsRequester extends AuthorizedHttpsRequester {

    public AuthorizedGetHttpsRequester() {
        method(HttpMethod.GET);
    }
}
