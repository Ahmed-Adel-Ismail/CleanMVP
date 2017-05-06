package com.base.usecases.requesters.server.ssl;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAcceptedLanguage;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAuthorizationToken;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderContentType;

/**
 * an authorized version for the {@link HttpsRequester}, where it contains the token header and the
 * other default headers (like content type or similar headers)
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
public class AuthorizedHttpsRequester extends HttpsRequester {

    public AuthorizedHttpsRequester() {

    }

    @Override
    public void initialize(Object requestUrlLocator) {
        super.initialize(requestUrlLocator);
        header(new HttpHeaderAuthorizationToken());
        header(new HttpHeaderContentType());
        header(new HttpHeaderAcceptedLanguage());
    }

    /**
     * create a {@link HttpsRequester} with the default configuration ... this means that the
     * {@link HttpMethod} for your requests will be {@link HttpMethod#POST}
     *
     * @param requestUrlLocator the {@link RequestUrlLocator} to generate urls
     * @deprecated use no-args constructor then call {@link #initialize(Object)} instead
     */
    public AuthorizedHttpsRequester(@NonNull RequestUrlLocator requestUrlLocator) {
        super(requestUrlLocator);
        header(new HttpHeaderAuthorizationToken());
        header(new HttpHeaderContentType());
        header(new HttpHeaderAcceptedLanguage());
    }
}
