package com.base.presentation.repos.json;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CallBackCancelException;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.interfaces.Initializable;
import com.base.abstraction.logs.Logger;
import com.base.usecases.callbacks.Callback;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.base.EntityGateway;

import java.io.Serializable;

/**
 * a {@link Command} to process requests from an {@link EntityGateway}, make sure to invoke
 * {@link JsonRequest#clear()} to cancel the callback of the requests and
 * free resources as soon as possible
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
abstract class AbstractJsonResponse<T extends Serializable> implements
        Clearable,
        Initializable<CallbackDispatcher>,
        Command<ResponseMessage, Void> {

    CallbackDispatcher callbackDispatcher;
    private Command<String, ?> errorParser;


    public void setErrorParser(Command<String, ?> errorParser) {
        this.errorParser = errorParser;
    }

    @Override
    @CallSuper
    public void initialize(CallbackDispatcher callbackDispatcher) {
        this.callbackDispatcher = callbackDispatcher;
    }

    @Override
    public Void execute(ResponseMessage message) {
        if (callbackDispatcher != null) {
            parseJsonAndNotifyCallback(message);
        }
        return null;
    }

    private void parseJsonAndNotifyCallback(ResponseMessage message) {
        AbstractResponseMessageParser parser = createResponseMessageParser();
        parser.setErrorParser(errorParser);
        ResponseMessage responseMessage = parser.execute(message);
        try {
            responseMessage = onResponseReceived(responseMessage);
            ResponseMessageChecker handler = new ResponseMessageChecker(responseMessage);
            handler.execute(callbackDispatcher);
        } catch (CallBackCancelException ex) {
            Logger.getInstance().exception(ex);
        }
    }

    @NonNull
    abstract AbstractResponseMessageParser createResponseMessageParser();


    /**
     * implement this method if you want to add custom implementation to the response before
     * notifying the {@link Callback} ... notice that the
     * passed {@link ResponseMessage#isSuccessful()} may return {@code false}, you have to check
     * for it first
     *
     * @param responseMessage the {@link ResponseMessage} received
     * @return the {@link ResponseMessage} to be sent to the {@link Callback}, if nothing is
     * changed, you can return the same {@link ResponseMessage} passed in the parameter
     */
    protected ResponseMessage onResponseReceived(ResponseMessage responseMessage) throws
            CallBackCancelException {
        // template method
        return responseMessage;
    }


    @Override
    @CallSuper
    public void clear() {
        callbackDispatcher = null;
    }

}
