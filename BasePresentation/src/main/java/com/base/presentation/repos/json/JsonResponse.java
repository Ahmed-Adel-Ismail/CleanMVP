package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.interfaces.ExecutableCommand;
import com.base.abstraction.commands.Command;
import com.base.usecases.callbacks.CallbackDispatcher;
import com.base.presentation.repos.base.RepositoryResponsesHandler;
import com.base.usecases.requesters.base.EntityGateway;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * a {@link Command} to process requests from an {@link EntityGateway}, make sure to invoke
 * {@link JsonRequest#clear()} to cancel the callback of the requests and
 * free resources as soon as possible
 * <p>
 * Created by Ahmed Adel on 9/18/2016.
 */
public abstract class JsonResponse<T extends Serializable> extends AbstractJsonResponse<T> {


    /**
     * create a {@link JsonResponse}
     *
     * @param callbackDispatcher the {@link CallbackDispatcher} which is the
     *                           {@link com.base.presentation.repos.base.Repository} in most cases
     * @deprecated after creating {@link RepositoryResponsesHandler}, you can use
     * {@link #JsonResponse()} the {@link RepositoryResponsesHandler} subclasses and annotate
     * them with {@link ExecutableCommand}
     */
    @Deprecated
    protected JsonResponse(CallbackDispatcher callbackDispatcher) {
        this.callbackDispatcher = callbackDispatcher;
    }

    /**
     * use this constructor from {@link RepositoryResponsesHandler}
     * classes
     */
    protected JsonResponse() {

    }


    @NonNull
    ResponseMessageParser<T> createResponseMessageParser() {
        return new ResponseMessageParser<>(getTypeToken());
    }


    /**
     * create a {@link TypeToken} that knows the type of the successful response
     *
     * @return a {@link TypeToken} of the successful response type
     */
    protected abstract TypeToken<T> getTypeToken();


}