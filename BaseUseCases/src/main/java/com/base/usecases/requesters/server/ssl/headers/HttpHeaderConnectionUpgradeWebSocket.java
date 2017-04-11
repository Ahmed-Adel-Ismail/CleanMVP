package com.base.usecases.requesters.server.ssl.headers;

import com.base.usecases.requesters.server.base.HttpHeaders;

/**
 * Created by Ahmed Adel on 1/24/2017.
 */
public class HttpHeaderConnectionUpgradeWebSocket implements HttpHeaderAction {
    @Override
    public HttpHeaders execute(HttpHeaders headers) {
        headers.put("Connection", "Upgrade");
        return headers;
    }
}
