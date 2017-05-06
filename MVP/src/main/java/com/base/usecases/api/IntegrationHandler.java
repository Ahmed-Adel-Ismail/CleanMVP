package com.base.usecases.api;

import com.base.abstraction.annotations.readers.ClassAnnotationReader;
import com.base.abstraction.api.usecases.AbstractIntegrationHandler;
import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.exceptions.annotations.AnnotationNotDeclaredException;
import com.base.abstraction.exceptions.failures.OAuth2Failure;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.presentation.failures.OAuth2Handler;
import com.base.usecases.annotations.HttpRequestUrlLocator;
import com.base.entities.auth.AbstractRefreshToken;
import com.base.usecases.annotations.RefreshTokenEntity;
import com.base.usecases.annotations.WebSocketUrlLocator;
import com.base.usecases.requesters.server.base.ServerRequester;

/**
 * the handler class for integration layer that holds methods that cannot be declared in
 * abstraction layer due to dependencies
 * <p>
 * you must declare the following annotations on the {@link App} sub-class in your project
 * for integration layer to take effect
 * <p>
 * <u>optional annotations :</u><br>
 * {@link HttpRequestUrlLocator} : the class that locates the production/test base https urls,
 * this is used with {@link ServerRequester} sub-classes<br>
 * {@link WebSocketUrlLocator} : the class that locates the production/test base wss urls<br>
 * {@link RefreshTokenEntity} : the implementer for {@link AbstractRefreshToken} class,
 * this is mandatory if you will use {@link OAuth2Failure} and it's
 * {@link OAuth2Handler}<br>
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 *
 * @see RefreshTokenEntity
 * @see HttpRequestUrlLocator
 * @see WebSocketUrlLocator
 */
public class IntegrationHandler implements AbstractIntegrationHandler {

    private RequestUrlLocator httpsUrlLocator;
    private RequestUrlLocator wssUrlLocator;
    private Class<? extends AbstractRefreshToken> refreshTokenClass;

    public IntegrationHandler() {
        wssUrlLocator = readWssUrlLocatorAnnotation();
        httpsUrlLocator = readHttpsUrlLocatorAnnotation();
        refreshTokenClass = readRefreshTokenAnnotation();
    }

    private RequestUrlLocator readWssUrlLocatorAnnotation() {
        RequestUrlLocator locator = null;
        if (App.getInstance().getClass().isAnnotationPresent(WebSocketUrlLocator.class)) {
            locator = doReadWssUrlLocatorAnnotation();
        } else {
            Logger.getInstance().error(getClass(), "no WSS Url locator declared, WebSocket " +
                    "requester classes will not work properly ");
        }
        return locator;
    }

    private RequestUrlLocator doReadWssUrlLocatorAnnotation() {
        Class<? extends RequestUrlLocator> klass;
        klass = new ClassAnnotationReader<>(WebSocketUrlLocator.class)
                .execute(App.getInstance())
                .value();
        return new Initializer<RequestUrlLocator>()
                .execute(klass);
    }

    private RequestUrlLocator readHttpsUrlLocatorAnnotation() {
        if (App.getInstance().getClass().isAnnotationPresent(HttpRequestUrlLocator.class)) {
            return doReadHttpsUrlLocatorAnnotation();
        } else {
            return null;
        }

    }

    private RequestUrlLocator doReadHttpsUrlLocatorAnnotation() {
        Class<? extends RequestUrlLocator> klass;
        klass = new ClassAnnotationReader<>(HttpRequestUrlLocator.class)
                .execute(App.getInstance())
                .value();
        return new Initializer<RequestUrlLocator>().execute(klass);
    }

    private Class<? extends AbstractRefreshToken> readRefreshTokenAnnotation() {
        if (App.getInstance().getClass().isAnnotationPresent(RefreshTokenEntity.class)) {
            return new ClassAnnotationReader<>(RefreshTokenEntity.class)
                    .execute(App.getInstance())
                    .value();
        } else {
            Exception ex = new IllegalStateException("no Refresh token entity declared for the app");
            Logger.getInstance().error(getClass(), ex.getMessage());
            return null;
        }

    }

    /**
     * get the default {@link RequestUrlLocator} for the application Https requests
     *
     * @return the {@link RequestUrlLocator} to be used
     */
    public final RequestUrlLocator getHttpRequestUrlLocator() {
        if (httpsUrlLocator == null) {
            throw new UnsupportedOperationException("you must declare @"
                    + HttpRequestUrlLocator.class.getSimpleName()
                    + " before accessing this method");
        }
        return httpsUrlLocator;

    }

    /**
     * get the default {@link RequestUrlLocator} for the application Wss requests
     *
     * @return the {@link RequestUrlLocator} to be used
     */
    public final RequestUrlLocator getWebSocketUrlLocator() {
        if (wssUrlLocator == null) {
            throw new UnsupportedOperationException("you must declare @"
                    + WebSocketUrlLocator.class.getSimpleName()
                    + " before accessing this method");
        }
        return wssUrlLocator;
    }

    /**
     * get the proper {@link RequestUrlLocator} for the passed Object based on it's declared
     * annotation
     *
     * @param annotatedClass the Object that is annotated with {@link RequestUrlLocator} related
     *                       annotations, like {@link HttpRequestUrlLocator}, {@link WebSocketUrlLocator}, and
     *                       similar annotations
     * @return the related {@link RequestUrlLocator}, or {@code null} if non is declared
     */
    public RequestUrlLocator getServerRequesterUrlLocator(Object annotatedClass) {

        Class<?> klass = annotatedClass.getClass();
        if (klass.isAnnotationPresent(HttpRequestUrlLocator.class)) {
            return getHttpRequestUrlLocator();
        } else if (klass.isAnnotationPresent(WebSocketUrlLocator.class)) {
            return getWebSocketUrlLocator();
        } else {
            logAnnotationNotDeclaredMessage();
            return null;
        }
    }

    private void logAnnotationNotDeclaredMessage() {
        Exception ex = new AnnotationNotDeclaredException(HttpRequestUrlLocator.class);
        Logger.getInstance().info(getClass(), ex.getMessage());
    }

    /**
     * create a {@link AbstractRefreshToken} implementer
     *
     * @return the {@link AbstractRefreshToken} class per project
     */
    public final AbstractRefreshToken createRefreshableToken() {

        if (refreshTokenClass == null) {
            throw new UnsupportedOperationException("you must declare @"
                    + RefreshTokenEntity.class.getSimpleName() + " before accessing this method");
        }
        return new Initializer<AbstractRefreshToken>().execute(refreshTokenClass);
    }

}
