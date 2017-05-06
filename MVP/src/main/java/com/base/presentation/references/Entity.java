package com.base.presentation.references;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.models.Model;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * a {@link Property} that can be retrieved from a {@link Repository}, you can set the
 * {@link Command} that will create a {@link RequestMessage} in
 * {@link #onRequestMessage(Command)}  to create your {@link RequestMessage} that will be
 * passed to {@link Model#requestFromRepository(long, RequestMessage)}
 * <p>
 * Created by Ahmed Adel on 12/29/2016.
 *
 * @param <T> the type of the value stored in this Entity
 */
public class Entity<T> extends Property<T> implements Serializable {


    private boolean retryPolicy;
    private Serializable retryMessageContent;
    private long retryIntervalMillis;
    private Disposable retryDisposable;

    private transient Model model;
    private long requestId;
    private boolean requesting;
    private Command<Serializable, RequestMessage> onRequestMessage;
    private Command<Entity<T>, Boolean> onIsRequestRequired;
    private Command<RequestParam<T>, ?> onRequest;
    private Command<ResponseParam<T>, ?> onResponse;
    private Command<ResponseMessage, Boolean> onNext;
    private Command<ResponseMessage, ?> onComplete;
    private Command<RequestMessage, Void> requesterCommand;
    private Command<ResponseMessage, Void> responderCommand;

    public Entity() {

    }

    Entity(T object) {
        super(object);
    }

    public void initialize(long requestId, Model model) {
        this.requestId = requestId;
        this.model = model;
        this.requesterCommand = createRequesterCommand();
        this.responderCommand = createResponderCommand();
    }

    /**
     * this method is invoked in the {@link Command} set in {@link #onResponse(Command)},
     * which is invoked as soon as a response is received from the
     * {@link Repository}, the default is that it invoked the {@link #set(Object)} method
     * if the response is {@link ResponseMessage#isSuccessful()}
     *
     * @param responseMessage the {@link ResponseMessage}
     */
    private void onResponseReceived(ResponseMessage responseMessage) {
        if (responseMessage.isSuccessful()) {
            T result = responseMessage.getContent();
            set(result);
        }
    }


    protected final Model getModel() {
        return model;
    }

    public final long getRequestId() {
        return requestId;
    }

    public final Command<RequestMessage, Void> getRequesterCommand() {
        return requesterCommand;
    }

    public final Command<ResponseMessage, Void> getResponderCommand() {
        return responderCommand;
    }


    @NonNull
    private Command<RequestMessage, Void> createRequesterCommand() {
        return new Command<RequestMessage, Void>() {
            @Override
            public Void execute(RequestMessage p) {
                requestCommandImpl();
                return null;
            }
        };
    }

    protected void requestCommandImpl() {
        request();
    }

    /**
     * process a request using either using the {@link Command} set through
     * {@link #onRequest(Command)}, or directly through
     * {@link #requestFromRepository(Serializable)} if no
     * {@link Command} was setVariable
     */
    public final void request() {
        request(null);
    }

    /**
     * process a request using either using the {@link Command} set through
     * {@link #onRequest(Command)}, or directly through
     * {@link #requestFromRepository(Serializable)} if no
     * {@link Command} was setVariable
     *
     * @param messageContent the Object to be set as {@link RequestMessage#content}
     */
    public void request(Serializable messageContent) {
        processRequest(messageContent);
    }

    private void processRequest(Serializable messageContent) {
        if (model == null) {
            return;
        }

        if (onRequest != null) {
            invalidateAndExecuteOnRequest(messageContent);
        } else {
            updateRetryMessageAndRequestFromRepository(messageContent);
        }
    }

    private void invalidateAndExecuteOnRequest(Serializable messageContent) {
        invalidateRetryMessageAndRequestingState(messageContent);
        onRequest.execute(new RequestParam<>(this, messageContent));
    }

    /**
     * do actual request from {@link Repository}
     *
     * @param messageContent optional parameter to be set as the {@link RequestMessage#content}
     */
    public final void requestFromRepository(Serializable messageContent) {
        if (model != null) {
            updateRetryMessageAndRequestFromRepository(messageContent);
        }
    }

    protected void updateRetryMessageAndRequestFromRepository(Serializable messageContent) {
        invalidateRetryMessageAndRequestingState(messageContent);

        RequestMessage requestMessage = (onRequestMessage != null)
                ? onRequestMessage.execute(messageContent)
                : null;

        if (requestMessage == null && messageContent != null) {
            requestMessage = new RequestMessage.Builder().content(messageContent).build();
        }

        requesting = true;
        model.requestFromRepository(requestId, requestMessage);
    }

    private void invalidateRetryMessageAndRequestingState(Serializable messageContent) {
        if (retryPolicy) {
            retryMessageContent = messageContent;
        }
        requesting = false;
    }

    /**
     * set the {@link Command} that will create a {@link RequestMessage} for requesting from
     * repository
     *
     * @param onRequestMessage the {@link Command} that will create {@link RequestMessage},
     *                         it's first parameter will be passed the value that was expected
     *                         to be the {@link RequestMessage#content} ... this can be
     *                         {@code null}
     * @return {@code this} instance for chaining
     */
    public Entity<T> onRequestMessage(Command<Serializable, RequestMessage> onRequestMessage) {
        this.onRequestMessage = onRequestMessage;
        return this;
    }

    /**
     * request only if requesting this entity is required, this method will do nothing if
     * {@link #isRequestRequired()} returned {@code false}
     *
     * @return {@code true} if the request is processed, else {@code false} (which means nothing
     * happened)
     */
    public final boolean requestIfRequired() {
        if (isRequestRequired()) {
            request();
            return true;
        }
        return false;
    }

    /**
     * request only if requesting this entity is required, this method will do nothing if
     * {@link #isRequestRequired()} returned {@code false}
     *
     * @param messageContent optional Object to be set as {@link RequestMessage#content}
     * @return {@code true} if the request is processed, else {@code false} (which means nothing
     * happened)
     */
    public final boolean requestIfRequired(Serializable messageContent) {
        if (isRequestRequired()) {
            request(messageContent);
            return true;
        }
        return false;
    }

    /**
     * check if a request is required for this {@link Entity}
     *
     * @return {@code true} if this {@link Entity} requires a request, the default implementation for
     * this method is that .. it will return {@code true} if {@link #get()} method returned
     * {@code null} and there is no current request processing
     */
    public boolean isRequestRequired() {
        if (onIsRequestRequired != null) {
            return onIsRequestRequired.execute(this);
        } else {
            return get() == null && !requesting;
        }
    }

    /**
     * set the {@link Command} to be executed when invoking {@link #isRequestRequired()}
     *
     * @param onIsRequestRequired the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public Entity<T> onIsRequestRequired(Command<Entity<T>, Boolean> onIsRequestRequired) {
        this.onIsRequestRequired = onIsRequestRequired;
        return this;
    }

    /**
     * set the {@link Command} that will be executed when requesting from server, if
     * you set this {@link Command}, you will need to invoke
     * {@link #requestFromRepository(Serializable)} manually
     *
     * @param onRequest the {@link Command} that will be executed when a request to server will be
     *                  done
     * @return {@code this} instance for chaining
     */
    public Entity<T> onRequest(Command<RequestParam<T>, ?> onRequest) {
        this.onRequest = onRequest;
        return this;
    }

    /**
     * set the {@link Command} that will be executed as soon as a response is received,
     * the default implementation of this {@link Command} is
     * {@link #onResponseReceived(ResponseMessage)}, which sets the current {@link Entity} content
     * when the response is successful, this {@link Command} is executed before
     * {@link #onNext(Command)} and {@link #onComplete(Command)}
     *
     * @param onResponse the {@link Command} to be executed
     * @return {@code this} instance for chaining
     */
    public Entity<T> onResponse(Command<ResponseParam<T>, ?> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    /**
     * check if this {@link Entity} is currently processing a request or not
     *
     * @return {@code true} if a request is currently ongoing, else {@code false}
     */
    public boolean isRequesting() {
        return requesting;
    }

    /**
     * set weather this {@link Entity} will attempt to retry requesting failed responses or not
     *
     * @param retryPolicy the retry policy
     */
    public void setRetryPolicy(boolean retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    /**
     * set the retry wait interval
     *
     * @param retryIntervalMillis the retry interval in milliseconds
     */
    public void setRetryIntervalMillis(long retryIntervalMillis) {
        this.retryIntervalMillis = retryIntervalMillis;
    }

    @NonNull
    private Command<ResponseMessage, Void> createResponderCommand() {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage responseMessage) {
                doResponse(responseMessage);
                return null;
            }


        };
    }

    protected void doResponse(ResponseMessage responseMessage) {

        if (!responseMessage.isSuccessful() && retryPolicy) {
            if (isRetrying()) return;
        } else {
            retryMessageContent = null;
        }

        requesting = false;
        if (model == null) {
            // cleared
            return;
        }


        if (onResponse != null) {
            onResponse.execute(new ResponseParam<>(this, responseMessage));
        } else {
            onResponseReceived(responseMessage);
        }


        if (onNext == null || onNext.execute(responseMessage)) {
            notifyOnRepositoryResponse(responseMessage);
        }

        if (onComplete != null) {
            onComplete.execute(responseMessage);
        }

    }

    private boolean isRetrying() {
        if (retryIntervalMillis == 0) {
            processRequest(retryMessageContent);
        } else {
            retryLastRequestAfterDelay();
        }
        return retryPolicy;
    }


    private void retryLastRequestAfterDelay() {

        if (retryDisposable != null) {
            retryDisposable = clearDisposableAndReturnNull(retryDisposable);
        }

        retryDisposable = Observable.create(sleepForRetryInterval())
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.trampoline())
                .subscribe(retryLastRequestAfterSleep());

    }

    @NonNull
    private ObservableOnSubscribe<Integer> sleepForRetryInterval() {
        return new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                try {
                    Thread.sleep(retryIntervalMillis);
                } catch (InterruptedException ex) {
                    Logger.getInstance().exception(ex);
                }
                e.onNext(0); // no retry limit, always pass 0
            }
        };
    }


    @NonNull
    private Consumer<Integer> retryLastRequestAfterSleep() {
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer retryTimesIndex) throws Exception {

                if (retryDisposable != null) {
                    retryDisposable = clearDisposableAndReturnNull(retryDisposable);
                }

                try {
                    processRequest(retryMessageContent);
                } catch (Throwable e) {
                    Logger.getInstance().exception(e);
                }

            }
        };
    }

    /**
     * set the {@link Command} that is called directly after {@link #onResponseReceived(ResponseMessage)},
     * if this {@link Command} returned {@code true}, {@link #notifyOnRepositoryResponse(ResponseMessage)}
     * will be invoked next, then the {@link Command} set in {@link #onComplete(Command)} will be
     * invoked after it, else the {@link Command} set in
     * {@link #onComplete(Command)} will be invoked without invoking
     * {@link #notifyOnRepositoryResponse(ResponseMessage)}
     *
     * @param onNext the {@link Command} that will be invoked after
     *               {@link #onResponseReceived(ResponseMessage)}, this {@link Command} can return
     *               {@code true} to invoke {@link #notifyOnRepositoryResponse(ResponseMessage)}
     *               after it finishes, or return {@code false} to go directly to invoke
     *               the {@link Command} set through {@link #onComplete(Command)}
     * @return {@code this} instance for chaining
     */
    public Entity<T> onNext(Command<ResponseMessage, Boolean> onNext) {
        this.onNext = onNext;
        return this;
    }

    /**
     * a method that invokes {@link Model#notifyOnRepositoryResponse(ResponseMessage)} if
     * the {@link Command} set in {@link #onNext(Command)} returned {@code true},
     * or if there was no {@link Command} set in {@link #onNext(Command)} ... if you will override
     * this method, make sure you check if {@link #getModel()} does not return {@code null}
     *
     * @param responseMessage the {@link ResponseMessage} received
     */
    protected void notifyOnRepositoryResponse(ResponseMessage responseMessage) {
        if (model != null) {
            model.notifyOnRepositoryResponse(responseMessage);
        }
    }


    /**
     * a {@link Command} that is invoked at the very end of handling the response,
     * you can take action for doing something after notifying the above layer (or not based
     * on the result of the {@link Command} set in {@link #onNext(Command)} ... like for example
     * doing another {@link Entity#request()} for example ... it is guaranteed that this method
     * will always be invoked
     *
     * @param onComplete the optional {@link Command} to be executed at the end of the operation
     */
    public Entity<T> onComplete(Command<ResponseMessage, ?> onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    @CallSuper
    public void clear() {
        super.clear();
        model = null;
        onRequest = null;
        onRequestMessage = null;
        onResponse = null;
        onNext = null;
        onComplete = null;
        requesterCommand = null;
        responderCommand = null;
        requesting = false;
        if (retryDisposable != null) {
            retryDisposable = clearDisposableAndReturnNull(retryDisposable);
        }

    }

    private Disposable clearDisposableAndReturnNull(Disposable retryDisposable) {
        if (!retryDisposable.isDisposed()) {
            retryDisposable.dispose();
        }
        return null;
    }

    /**
     * a class that is used as parameters for the {@link #onRequest(Command)} command
     *
     * @param <E> the type of the value stored in the {@link Entity}
     */
    public static class RequestParam<E> {

        public final Entity<E> entity;
        public final Serializable messageContent;

        RequestParam(Entity<E> entity, Serializable messageContent) {
            this.entity = entity;
            this.messageContent = messageContent;
        }
    }

    /**
     * a class that is used as parameters for the {@link #onResponse(Command)} command
     *
     * @param <E> the type of the value stored in the {@link Entity}
     */
    public static class ResponseParam<E> {

        public final Entity<E> entity;
        public final ResponseMessage responseMessage;

        ResponseParam(Entity<E> entity, ResponseMessage messageContent) {
            this.entity = entity;
            this.responseMessage = messageContent;
        }
    }

    @Override
    public int hashCode() {
        return (int) requestId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Entity
                && requestId == ((Entity) obj).requestId
                && hasSameContent(((Entity) obj));
    }

    private boolean hasSameContent(@NonNull Entity obj) {
        if (get() != null) {
            return get().equals(obj.get());
        } else {
            return obj.get() == null;
        }

    }


}
