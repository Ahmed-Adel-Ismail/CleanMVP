package com.base.usecases.requesters.server.ssl;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.server.base.HttpHeaders;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAction;
import com.base.usecases.annotations.HttpRequestUrlLocator;
import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.base.ServerRequester;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * An Https requester that makes a secure connection with the server ... this class uses the
 * deprecated {@link org.apache.http.client.HttpClient}
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
@HttpRequestUrlLocator
public class HttpsRequester extends ServerRequester {


    private static final HttpMethod DEFAULT_HTTP_METHOD = HttpMethod.POST;

    private int connectionTimeout = DEFAULT_TIME_OUT_MILLIS;
    private int socketTimeout = DEFAULT_TIME_OUT_MILLIS;
    private HttpMethod method = DEFAULT_HTTP_METHOD;


    /**
     * create a {@link HttpsRequester} with the default configuration ... this means that the
     * {@link HttpMethod} for your requests will be {@link HttpMethod#POST}
     *
     * @param requestUrlLocator the {@link RequestUrlLocator} to generate urls
     * @deprecated use no-args constructor then use {@link #initialize(Object)} instead
     */
    public HttpsRequester(@NonNull RequestUrlLocator requestUrlLocator) {
        super(requestUrlLocator);
    }

    public HttpsRequester() {

    }

    /**
     * set the {@link HttpMethod}, the default method is {@link HttpMethod#POST}
     *
     * @param method the {@link HttpMethod} for requests
     * @return {@code this} instance for chaining
     */
    public final HttpsRequester method(@NonNull HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public final HttpsRequester header(HttpHeaderAction httpHeaderAction) {
        super.header(httpHeaderAction);
        return this;
    }

    /**
     * set the connection timeout, the default is {@link #DEFAULT_TIME_OUT_MILLIS}
     *
     * @param connectionTimeout the new connection timeout value
     * @return {@code this} instance for chaining
     */
    public final HttpsRequester connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * set the socket timeout, the default is {@link #DEFAULT_TIME_OUT_MILLIS}
     *
     * @param socketTimeout the new socket timeout value
     * @return {@code this} instance for chaining
     */
    public final HttpsRequester socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    @Override
    public Void execute(Event event) {
        long eventId = event.getId();
        HttpHeaders headersCopy = headers.copy().update();
        HttpRequestBase request = createRequestGenerator(event, headersCopy).execute(method);
        HttpResponse response = createRequestExecutor().execute(request);
        ResponseMessage responseMessage = createResponseReader(eventId).execute(response);
        notifyCallback(createCallbackEvent(eventId, responseMessage));
        return null;
    }

    private HttpRequestGenerator createRequestGenerator(Event event, HttpHeaders headers) {
        Message message = event.getMessage();
        String baseUrl = getRequestUrlLocator().execute(event.getId());
        String url = new HttpUrlGenerator(baseUrl).execute(message);
        return new HttpRequestGenerator(url, message).headers(headers);
    }

    private HttpRequestExecutor createRequestExecutor() {
        return new HttpRequestExecutor(connectionTimeout, socketTimeout);
    }

    @NonNull
    private HttpResponseReader createResponseReader(long eventId) {
        return new HttpResponseReader(eventId);
    }


    private Event createCallbackEvent(long eventId, ResponseMessage responseMessage) {
        return new Event.Builder(eventId).message(responseMessage).build();
    }
}
