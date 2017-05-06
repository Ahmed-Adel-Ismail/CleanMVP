package com.base.presentation.references;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.exceptions.references.RequestInProgressException;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;

import java.io.Serializable;

/**
 * an {@link Entity} that acts as a requester for multiple requests, not one request only,
 * it is a stateless {@link Entity} that removes it's {@link #onNext(Command)}
 * and {@link #onComplete(Command)} after every request
 * <p>
 * Created by Ahmed Adel on 1/6/2017.
 */
public class Requester<T> extends Entity<T> {

    /**
     * @throws RequestInProgressException if a request is currently in progress
     */
    @Override
    public Requester<T> onNext(Command<ResponseMessage, Boolean> onNext)
            throws RequestInProgressException {
        if (isRequesting()) {
            throw new RequestInProgressException(getRequestId());
        }
        super.onNext(onNext);
        return this;
    }

    /**
     * @throws RequestInProgressException if a request is currently in progress
     */
    @Override
    public Requester<T> onComplete(Command<ResponseMessage, ?> onComplete) {
        if (isRequesting()) {
            throw new RequestInProgressException(getRequestId());
        }
        super.onComplete(onComplete);
        return this;
    }

    /**
     * @throws RequestInProgressException if a request is currently in progress
     */
    @Override
    protected void updateRetryMessageAndRequestFromRepository(Serializable messageContent) {
        if (isRequesting()) {
            throw new RequestInProgressException(getRequestId());
        }
        super.updateRetryMessageAndRequestFromRepository(messageContent);
    }

    @Override
    protected void requestCommandImpl() {
        try {
            super.requestCommandImpl();
        } catch (RequestInProgressException e) {
            Logger.getInstance().exception(e);
        }
    }

    @Override
    protected void doResponse(ResponseMessage responseMessage) {
        super.doResponse(responseMessage);
        onNext(null).onComplete(null);
    }

    @Override
    public Requester<T> onIsRequestRequired(Command<Entity<T>, Boolean> onIsRequestRequired) {
        super.onIsRequestRequired(onIsRequestRequired);
        return this;
    }

    @Override
    public Requester<T> onRequest(Command<RequestParam<T>, ?> onRequest) {
        super.onRequest(onRequest);
        return this;
    }

    @Override
    public Requester<T> onRequestMessage(Command<Serializable, RequestMessage> onRequestMessage) {
        super.onRequestMessage(onRequestMessage);
        return this;
    }
}
