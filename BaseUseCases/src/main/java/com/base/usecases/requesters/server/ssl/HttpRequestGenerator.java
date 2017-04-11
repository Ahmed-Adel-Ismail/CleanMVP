package com.base.usecases.requesters.server.ssl;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.usecases.requesters.server.base.HttpHeaders;
import com.base.usecases.requesters.server.base.HttpMethod;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.util.Arrays;


/**
 * a Class to createNativeMethod an Http request
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 *
 * @see HttpMethod
 */
@SuppressWarnings("deprecation")
class HttpRequestGenerator implements Command<HttpMethod, HttpRequestBase> {


    private String url;
    private HttpHeaders headers;
    private String stringRequestBody;

    HttpRequestGenerator(String url, Message message) {
        this.url = url;
        this.stringRequestBody = message.getContent();
    }

    HttpRequestGenerator headers(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public HttpRequestBase execute(HttpMethod method) {

        HttpRequestBase request = method.createNativeMethod(url);

        if (hasHeaders()) {
            request = headers.fill(request);
        }

        if (hasRequestBody(request)) {
            request = addRequestBody((HttpEntityEnclosingRequestBase) request);
        }
        logRequest(request);

        return request;
    }


    private boolean hasHeaders() {
        return headers != null && !headers.isEmpty();
    }


    private boolean hasRequestBody(HttpRequestBase request) {
        return stringRequestBody != null && request instanceof HttpEntityEnclosingRequestBase;
    }

    private HttpRequestBase addRequestBody(HttpEntityEnclosingRequestBase request) {
        try {
            request.setEntity(new StringEntity(stringRequestBody, "utf-8"));
        } catch (Throwable e) {
            throwRuntimeExceptionForEncoding();
        }
        return request;
    }

    private void throwRuntimeExceptionForEncoding() {
        throw new RuntimeException("cannot encode the post/put StringEntity as utf-8 : "
                + stringRequestBody);
    }

    private void logRequest(HttpRequestBase request) {
        Logger.getInstance().error(getClass(), "url : " + request.getURI());
        Logger.getInstance().error(getClass(), "headers : " + Arrays.deepToString(request.getAllHeaders()));
        if (hasRequestBody(request)) {
            logEnclosedEntity((HttpEntityEnclosingRequestBase) request);
        }

    }

    private void logEnclosedEntity(HttpEntityEnclosingRequestBase request) {
        HttpEntity httpEntity = request.getEntity();

        if (httpEntity == null) {
            return;
        }

        Logger.getInstance().error(getClass(), "body : " +
                new HttpEntityReader().execute(httpEntity));
    }


}
