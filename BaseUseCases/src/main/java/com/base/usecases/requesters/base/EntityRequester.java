package com.base.usecases.requesters.base;

import android.support.annotation.CallSuper;

import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Clearable;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.server.base.ServerRequester;
import com.base.usecases.requesters.server.mocks.MockedEntitiesRegistry;

import java.lang.ref.WeakReference;

/**
 * An interface implemented by any class that will hold a request to an Entities source like
 * server or database or similar sources, to notify the {@link Callback} of this class you
 * should invoke {@link #notifyCallback(Event)}
 * <p/>
 * if you use this class through an {@link EntityGateway},
 * you wont need to invoke {@link #setCallback(Callback)}
 * <p/>
 * do not worry about Main onThread and background onThread if this class is used from inside a
 * {@link EntityGateway}
 * <p/>
 * if you use this class with an {@link EntityGateway} as well, you wont need to invoke
 * {@link #setCallback(Callback)}, else you must invoke this method before invoking
 * {@link #execute(Event)}
 * <p/>
 * Created by Ahmed Adel on 9/20/2016.
 *
 * @see ServerRequester
 */
public abstract class EntityRequester implements Requester, CallbackDispatcher, Clearable {

    private final CallbackDispatcher.ConcreteImplementer callbackDispatcher;
    private final MockedEntitiesRegistry mockedEntitiesRegistry;

    /**
     * createNativeMethod a {@link EntityRequester} that will start accessing a data source ... if you want to
     * access multiple data sources (like server and database), you should make multiple
     * {@link EntityRequester} classes, and not in the single class
     */
    protected EntityRequester() {
        this.callbackDispatcher = new CallbackDispatcher.ConcreteImplementer();
        this.mockedEntitiesRegistry = new MockedEntitiesRegistry();
    }


    /**
     * set the {@link Callback}, if you use this class through an {@link EntityGateway},
     * you wont need to invoke this method
     * <p/>
     * notice that this {@link Callback} is held in a {@link WeakReference}, so you might
     * want to hold reference to this call back in your class so that it wont be set to
     * {@code null} before the {@link #execute(Event)} method finishes execution
     *
     * @param callback a {@link Callback} that will be updated with results
     */
    public final void setCallback(Callback callback) {
        callbackDispatcher.setCallback(callback);
    }

    public final Callback getCallback() {
        return callbackDispatcher.getCallback();
    }

    /**
     * notify the {@link Callback} listener0
     *
     * @param event the {@link Event} to notify this {@link Callback} with
     */
    public final void notifyCallback(Event event) {
        callbackDispatcher.notifyCallback(event);
    }


    protected void putAllMockedEntitiesRegistry(MockedEntitiesRegistry mockedEntitiesRegistry) {
        this.mockedEntitiesRegistry.putAll(mockedEntitiesRegistry);
    }

    protected MockedEntitiesRegistry getMockedEntitiesRegistry() {
        return mockedEntitiesRegistry;
    }

    /**
     * for {@link EntityRequester} sub-classes, they should createNativeMethod an {@link Event} that
     * holds it's {@link Event#getId()} as the request ID, and also it should contain a
     * {@link com.base.usecases.events.ResponseMessage} that it's
     * {@link ResponseMessage#getId()} is the same request ID as well ...
     * then they should call {@link #notifyCallback(Event)} with this {@link Event} as a parameter
     */
    @Override
    public abstract Void execute(Event event);

    @Override
    @CallSuper
    public void clear() {
        mockedEntitiesRegistry.clear();
        callbackDispatcher.clear();
    }
}
