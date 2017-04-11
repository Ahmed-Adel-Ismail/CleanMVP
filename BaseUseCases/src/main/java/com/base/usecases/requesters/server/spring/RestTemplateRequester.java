package com.base.usecases.requesters.server.spring;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.events.Event;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.failures.FailureMessageGenerator;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.base.ServerRequester;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * a {@link EntityRequester} that is implemented through {@link RestTemplate} class
 * in {@code Spring} framework
 * <p/>
 * This implementation makes a non-secure request and returns the response as a {@code String}
 * <p/>
 * Created by Ahmed Adel on 9/20/2016.
 */
@Deprecated
public class RestTemplateRequester extends ServerRequester {

    private HttpMethod method;


    /**
     * createNativeMethod a {@link RestTemplateRequester} that makes {@code POST} requests
     *
     * @param requestUrlLocator the {@link RequestUrlLocator} to generate URLs
     */

    public RestTemplateRequester(RequestUrlLocator requestUrlLocator) {
        this(requestUrlLocator, HttpMethod.POST);
    }

    /**
     * createNativeMethod a {@link RestTemplateRequester}
     *
     * @param requestUrlLocator the {@link RequestUrlLocator} to generate URLs
     * @param method            the {@link HttpMethod} for requests, weather it is post or put
     */
    public RestTemplateRequester(RequestUrlLocator requestUrlLocator, HttpMethod method) {
        super(requestUrlLocator);
        this.method = method;
    }

    @Override
    public final Void execute(final Event event) {

        long eventId = event.getId();
        String url = getRequestUrlLocator().execute(eventId);
        String requestString = event.getMessage().getContent();

        logRequestURL((int) eventId, url, requestString);

        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(1000 * 120);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(1000 * 120);

        restTemplate.setErrorHandler(createDefaultResponseErrorHandler());

        HttpEntity<String> requestEntity = createRequestEntity(requestString);

        logRequestEntity(url, requestEntity);


        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(url, method.createSpringMethod(),
                    requestEntity, String.class);
        } catch (Throwable e) {
            SystemLogger.getInstance().exception(e);
        }

        ResponseMessage responseMessage;
        if (responseEntity != null) {
            logResponseEntity(url, responseEntity);
            responseMessage = createResponseMessage(eventId, responseEntity);
        } else {
            responseMessage = new FailureMessageGenerator().execute(eventId);
        }
        Event responseEvent = new Event.Builder(eventId).message(responseMessage).build();
        notifyCallback(responseEvent);
        return null;
    }

    @NonNull
    private HttpEntity<String> createRequestEntity(String requestString) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        return new HttpEntity<>(requestString, httpHeaders);
    }

    @NonNull
    private ResponseErrorHandler createDefaultResponseErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                logErrorResponse(response);
            }

            private void logErrorResponse(ClientHttpResponse response) throws IOException {
                if (!App.getInstance().isDebugging()) {
                    return;
                }
                SystemLogger.getInstance().info(getClass(), "ClientHttpResponse.getStatusCode() : "
                        + response.getStatusCode());

                SystemLogger.getInstance().info(getClass(), "ClientHttpResponse.getHeaders() : "
                        + response.getHeaders());

                SystemLogger.getInstance().info(getClass(), "ClientHttpResponse.getBody() : "
                        + response.getBody());
            }
        };
    }


    private ResponseMessage createResponseMessage(long eventId, ResponseEntity<String> responseEntity) {
        ResponseMessage.Builder builder = new ResponseMessage.Builder();
        builder.id(eventId);
        builder.content(responseEntity.getBody());
        builder.successful(responseEntity.getStatusCode().is2xxSuccessful());
        builder.statusCode(responseEntity.getStatusCode().value());
        return builder.build();
    }

    private void logRequestURL(int eventId, String url, String requestString) {
        if (!App.getInstance().isDebugging()) {
            return;
        }
        SystemLogger.getInstance().info(getClass(), "requestId [" + url + "] " +
                AppResources.resourceEntryName(eventId));

        SystemLogger.getInstance().info(getClass(), "request [" + url + "] " +
                requestString);
    }

    private void logRequestEntity(String url, HttpEntity<String> requestEntity) {
        if (!App.getInstance().isDebugging()) {
            return;
        }
        SystemLogger.getInstance().info(getClass(), "requestEntity.getHeaders() [" + url + "] "
                + requestEntity.getHeaders());
        SystemLogger.getInstance().error(getClass(), "requestEntity.getBody() [" + url + "] "
                + requestEntity.getBody());
    }

    private void logResponseEntity(String url, ResponseEntity<String> responseEntity) {
        if (!App.getInstance().isDebugging()) {
            return;
        }
        SystemLogger.getInstance().error(getClass(), "responseEntity.getStatusCode() [" + url
                + "] " + responseEntity.getStatusCode());

        SystemLogger.getInstance().info(getClass(), "responseEntity.getHeaders() [" + url + "] "
                + responseEntity.getHeaders());

        SystemLogger.getInstance().error(getClass(), "responseEntity.getBody() [" + url + "] "
                + responseEntity.getBody());
    }

    public RestTemplateRequester method(HttpMethod method) {
        this.method = method;
        return this;
    }

}



