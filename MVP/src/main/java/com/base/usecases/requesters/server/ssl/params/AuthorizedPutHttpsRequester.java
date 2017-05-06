package com.base.usecases.requesters.server.ssl.params;

import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;

/**
 * a {@link AuthorizedHttpsRequester} for {@link HttpMethod#PUT} requests
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class AuthorizedPutHttpsRequester extends AuthorizedHttpsRequester {

    public AuthorizedPutHttpsRequester() {
        method(HttpMethod.PUT);
    }
}
