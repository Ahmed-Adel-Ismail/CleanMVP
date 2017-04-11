package com.base.usecases.requesters.server.base;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.aggregates.AggregateRemovable;
import com.base.abstraction.aggregates.KeyAggregateAddable;
import com.base.abstraction.interfaces.Emptyable;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAction;

import org.apache.http.client.methods.HttpRequestBase;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * a class that represents the headers of a Http request, used internally by the package, not
 * intended for being part of the API
 * <p>
 * Created by Ahmed Adel on 11/8/2016.
 */
@SuppressWarnings("deprecation")
public class HttpHeaders implements
        AggregateAddable<Boolean, HttpHeaderAction>,
        KeyAggregateAddable<String, String>,
        AggregateRemovable<String, String>,
        Emptyable {


    private final Map<String, String> map;
    private final Set<HttpHeaderAction> headerAction;

    public HttpHeaders() {
        this.headerAction = new HashSet<>();
        this.map = new LinkedHashMap<>();
    }

    @Override
    public String put(String key, String value) {
        return map.put(key, value);
    }


    @Override
    public boolean isEmpty() {
        return map.isEmpty() && headerAction.isEmpty();
    }

    /**
     * update the current {@link HttpHeaders} with the stored {@link HttpHeaderAction}
     *
     * @return {@code this} instance for chaining
     */
    public HttpHeaders update() {
        for (HttpHeaderAction action : headerAction) {
            action.execute(this);
        }
        return this;
    }

    /**
     * extract all the headers from the current {@link HttpHeaders} Object and add them to the
     * passed {@link HttpRequestBase}, you should invoke {@link #update()} before this method
     *
     * @param request the {@link HttpRequestBase} that will hold the headers
     */
    public HttpRequestBase fill(HttpRequestBase request) {

        for (Map.Entry<String, String> entry : map.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        return request;

    }

    @Override
    public String remove(String key) {
        return map.remove(key);
    }

    @Override
    public Boolean add(HttpHeaderAction httpHeaderAction) {
        return headerAction.add(httpHeaderAction);
    }

    public Map<String, String> asMap() {
        return map;
    }

    public HttpHeaders copy() {
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.headerAction.addAll(headerAction);
        return newHeaders;
    }
}
