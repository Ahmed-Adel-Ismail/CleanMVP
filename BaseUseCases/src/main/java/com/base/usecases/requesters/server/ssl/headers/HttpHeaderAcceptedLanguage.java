package com.base.usecases.requesters.server.ssl.headers;

import com.base.abstraction.system.App;
import com.base.usecases.requesters.server.base.HttpHeaders;

/**
 * a header to add the accepted language
 * <p>
 * Created by Ahmed Adel on 11/9/2016.
 */
public class HttpHeaderAcceptedLanguage implements HttpHeaderAction {

    @Override
    public HttpHeaders execute(HttpHeaders headers) {
        headers.put("Accept-Language", App.getInstance().getLocale().getLanguage());
        return headers;
    }
}
