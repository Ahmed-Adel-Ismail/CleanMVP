package com.base.usecases.requesters.server.ssl.headers;

import com.base.usecases.requesters.server.base.HttpHeaders;

/**
 * a {@link HttpHeaderAction} that adds the content type of the request as application/json
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
public class HttpHeaderContentType implements HttpHeaderAction {

    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String VALUE_CONTENT_TYPE = "application/json";

    @Override
    public HttpHeaders execute(HttpHeaders headers) {
        headers.put(KEY_CONTENT_TYPE, VALUE_CONTENT_TYPE);
        return headers;
    }
}
