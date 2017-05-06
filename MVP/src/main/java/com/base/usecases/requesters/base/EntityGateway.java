package com.base.usecases.requesters.base;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.base.abstraction.concurrency.WorkerThread;
import com.base.abstraction.events.Event;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.logs.SystemLogger;
import com.base.abstraction.system.AppResources;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.callbacks.CallbackHolder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * The Class responsible for communication with Server or database,
 * for any Library to be used, it should apply to the rules of communication for this class,
 * where any class that requires to communicate to server, it will notify this Class through
 * it's {@link #execute(Event)} with an {@link Event} that holds the request id and the
 * parameters as a {@link com.base.abstraction.events.Message} in this request
 * <p/>
 * for the Sub-classes, they should know the {@code URL} mapped to each {@link Event#getId()}
 * (or the query in case of database),
 * and then parse the passed {@link Event#getMessage()} to be the parameters appended to the
 * {@code URL} (or data required to search in a database), and then process the request action
 * <p/>
 * when the response is received (or an error occurs), the sub-class will notify the
 * {@link Callback} class through it's {@link Callback#onCallback(Event)},
 * adding a {@link ResponseMessage} as the Message for this
 * {@link Event} ... and the {@link Callback} will handle the rest
 * <p/>
 * Created by Ahmed Adel on 9/19/2016.
 */
public class EntityGateway implements
        Requester,
        Clearable,
        Callback,
        CallbackHolder {

    final CallbackDispatcher.ConcreteImplementer callbackDispatcher;
    private final RequestGroup requestGroup;
    private final WorkerThread handler;
    private final EntityRequester requester;
    private Disposable disposable;

    public EntityGateway(EntityRequester requester, Callback callback) {
        this.callbackDispatcher = new CallbackDispatcher.ConcreteImplementer();
        this.callbackDispatcher.setCallback(callback);
        this.requestGroup = new RequestGroup();
        this.requester = new EntityRequesterChecker(callback).execute(requester);
        this.requester.setCallback(this);
        this.handler = new WorkerThread(Looper.myLooper());
    }


    /**
     * for any request id, if it's request is already in progress, it will be ignored ...
     * to allow any request to be triggered even if it is already
     * processing, you will need to add it's id to the concurrent request ids through this
     * method
     *
     * @param id the request id to be permitted for concurrency
     */
    public final void addConcurrentRequestId(long id) {
        requestGroup.addConcurrentRequestId(id);
    }

    @Override
    public final void setCallback(Callback callback) {
        callbackDispatcher.setCallback(callback);
    }

    @Override
    public final Void execute(Event event) {
        handler.execute(createHandleOnUpdateRunnable(event));
        return null;
    }

    @NonNull
    Runnable createHandleOnUpdateRunnable(final Event event) {
        return new Runnable() {
            @Override
            public void run() {
                if (!requestGroup.contains(event)
                        || requestGroup.isConcurrentRequest(event.getId())) {
                    proceedToRequest();
                } else {
                    ignoreRequest(event.getId());
                }
            }

            private void proceedToRequest() {
                requestGroup.add(event);
                requestFromRequester(event);
            }

            private void ignoreRequest(long requestId) {
                SystemLogger.getInstance().error(EntityGateway.this.getClass(),
                        "request ID already in progress : " +
                                AppResources.resourceEntryName((int) requestId));
            }
        };
    }

    void requestFromRequester(final Event event) {
        disposable = Observable.just(event).subscribeOn(Schedulers.io())
                .subscribe(doRequest(), logError(), logRequestComplete());
    }

    @NonNull
    private Consumer<Event> doRequest() {
        return new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {
                if (requester != null) {
                    requester.execute(event);
                }
            }
        };
    }

    @NonNull
    private Consumer<Throwable> logError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.getInstance().exception(throwable);
            }
        };
    }

    @NonNull
    private Action logRequestComplete() {
        return new Action() {
            @Override
            public void run() throws Exception {
                Logger.getInstance().info(EntityGateway.class,
                        "requesterSubscriber onComplete()");
            }
        };
    }


    @Override
    public void onCallback(Event event) {
        handler.execute(createNotifyCallbackRunnable(event));
    }

    @NonNull
    Runnable createNotifyCallbackRunnable(final Event event) {
        return new Runnable() {
            @Override
            public void run() {
                if (requestGroup.contains(event)) {
                    removeRequestIdAndNotifyCallback(event);
                } else {
                    ignoreNotifyCallback(event.getId());
                }
            }
        };
    }

    private void removeRequestIdAndNotifyCallback(final Event event) {
        requestGroup.remove(event.getId());
        callbackDispatcher.notifyCallback(event);
    }

    private void ignoreNotifyCallback(long requestId) {
        SystemLogger.getInstance().error(getClass(),
                "request ID [" + requestId + "] not found, response ignored");
    }

    @Override
    public final void clear() {
        callbackDispatcher.clear();
        handler.clear();
        requestGroup.clear();
        requester.clear();
        if (disposable != null) {
            clearSubscription();
        }
    }

    private void clearSubscription() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }


}
