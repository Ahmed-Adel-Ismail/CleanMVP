package com.base.usecases.requesters.server.websocket;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAcceptedLanguage;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAuthorizationToken;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderConnectionUpgradeWebSocket;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderContentType;

/**
 * an authorized version for the {@link StaticWebSocketRequester}, where it contains the token header
 * and the other default headers (like content type or similar headers)
 * <p>
 * Created by Ahmed Adel on 1/22/2017.
 */
public class AuthorizedWebSocketsRequester extends StaticWebSocketRequester {

    @Override
    public void initialize(Object requestUrlLocator) {
        super.initialize(requestUrlLocator);
        header(new HttpHeaderAuthorizationToken());
        header(new HttpHeaderContentType());
        header(new HttpHeaderAcceptedLanguage());
        header(new HttpHeaderConnectionUpgradeWebSocket());
    }
}
