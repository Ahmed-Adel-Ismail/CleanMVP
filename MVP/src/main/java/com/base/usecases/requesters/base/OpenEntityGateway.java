package com.base.usecases.requesters.base;

import android.support.annotation.NonNull;

import com.base.abstraction.events.Event;
import com.base.usecases.callbacks.Callback;

/**
 * an Entity gateway for open connections, like web-sockets, it does not limit any requests or
 * responses
 * <p>
 * Created by Wafaa on 12/21/2016.
 */
public class OpenEntityGateway extends EntityGateway {


    public OpenEntityGateway(EntityRequester requester, Callback callback) {
        super(requester, callback);
    }

    @NonNull
    @Override
    Runnable createHandleOnUpdateRunnable(final Event event) {
        return new Runnable() {
            @Override
            public void run() {
                requestFromRequester(event);
            }
        };
    }

    @NonNull
    @Override
    Runnable createNotifyCallbackRunnable(final Event event) {
        return new Runnable() {
            @Override
            public void run() {
                callbackDispatcher.notifyCallback(event);
            }
        };
    }
}
