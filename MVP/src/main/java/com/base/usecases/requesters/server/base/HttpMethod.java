package com.base.usecases.requesters.server.base;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * The methods of making an Http request (post / put)
 * <p>
 * Created by Ahmed Adel on 10/18/2016.
 */
@SuppressWarnings("deprecation")
public enum HttpMethod {
    GET {
        @Override
        public HttpRequestBase createNativeMethod(String url) {
            return new HttpGet(url);
        }

        @Override
        public org.springframework.http.HttpMethod createSpringMethod() {
            return org.springframework.http.HttpMethod.GET;
        }
    },

    POST {
        @Override
        public HttpRequestBase createNativeMethod(String url) {
            return new HttpPost(url);
        }

        @Override
        public org.springframework.http.HttpMethod createSpringMethod() {
            return org.springframework.http.HttpMethod.POST;
        }
    },

    PUT {
        @Override
        public HttpRequestBase createNativeMethod(String url) {
            return new HttpPut(url);
        }

        @Override
        public org.springframework.http.HttpMethod createSpringMethod() {
            return org.springframework.http.HttpMethod.PUT;
        }
    };

    public abstract HttpRequestBase createNativeMethod(String url);

    public abstract org.springframework.http.HttpMethod createSpringMethod();
}
