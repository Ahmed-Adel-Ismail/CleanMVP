package com.base.usecases.requesters.base;

import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.concurrency.Future;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;

/**
 * a parameter to {@link EntityRequester} {@link Executor} methods, which will contain the
 * {@link RequestMessage}, and a {@link Future} to add a return type for it
 * <p>
 * Created by Ahmed Adel Ismail on 4/26/2017.
 */
public class RequesterContract {

    public final RequestMessage requestMessage;
    public final Future<ResponseMessage> responseFuture;


    RequesterContract(RequestMessage requestMessage, Future<ResponseMessage> responseFuture) {
        this.requestMessage = requestMessage;
        this.responseFuture = responseFuture;
    }

}
