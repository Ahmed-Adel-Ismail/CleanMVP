package com.base.usecases.requesters.server.base;

import android.support.annotation.NonNull;

import com.base.abstraction.api.usecases.RequestUrlLocator;
import com.base.abstraction.interfaces.Initializable;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAction;
import com.base.usecases.requesters.server.ssl.headers.HttpHeaderAuthorizationToken;

/**
 * a {@link EntityRequester} that manages communication with a sevrer
 * <p>
 * Created by Ahmed Adel on 10/19/2016.
 *
 * @see RequestUrlLocator
 */
public abstract class ServerRequester extends EntityRequester implements
        Initializable<RequestUrlLocator> {

    /**
     * the default timeout ... it is 2 minutes for now
     */
    protected static final int DEFAULT_TIME_OUT_MILLIS = 1000 * 120;
    private RequestUrlLocator requestUrlLocator;
    protected final HttpHeaders headers = new HttpHeaders();

    protected ServerRequester() {
    }

    /**
     * create a {@link ServerRequester} that will start requests from server ... if you want to
     * access multiple servers or request types , you should make multiple
     * {@link ServerRequester} classes, and not in the single class
     *
     * @param requestUrlLocator the {@link RequestUrlLocator} that will be used to generate URLs
     * @deprecated use no-args constructor then call {@link #initialize(RequestUrlLocator)}
     * instead of using this constructor
     */
    public ServerRequester(@NonNull RequestUrlLocator requestUrlLocator) {
        this.requestUrlLocator = requestUrlLocator;
    }

    @Override
    public void initialize(RequestUrlLocator requestUrlLocator) {
        this.requestUrlLocator = requestUrlLocator;
    }

    /**
     * get the {@link RequestUrlLocator} for this {@link EntityRequester}
     *
     * @return the {@link RequestUrlLocator} to createNativeMethod the request URL
     */
    protected final RequestUrlLocator getRequestUrlLocator() {
        return requestUrlLocator;
    }

    /**
     * add an {@link HttpHeaderAction} to handle creating a header in the https request
     *
     * @param httpHeaderAction a {@link HttpHeaderAction}, like
     *                         {@link HttpHeaderAuthorizationToken} for example
     * @return {@code this} instance for chaining
     * @see AuthorizedHttpsRequester
     */
    public ServerRequester header(HttpHeaderAction httpHeaderAction) {
        headers.add(httpHeaderAction);
        return this;
    }

}
