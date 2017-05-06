package com.base.presentation.repos.base;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.presentation.annotations.interfaces.Request;
import com.base.presentation.models.Model;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;

import java.lang.reflect.Field;

/**
 * a class to scan the {@link Request} annotation in the {@link Model} class of the
 * given {@link Repository}
 * <p>
 * Created by Ahmed Adel Ismail on 4/26/2017.
 */
class ModelRequestsAnnotationScanner extends FieldAnnotationScanner<Request> {


    private Repository repository;


    ModelRequestsAnnotationScanner(Repository repository) {
        super(Request.class);
        this.repository = repository;
    }


    @Override
    protected void onAnnotationFound(Field element, Request annotation) {

        final long requestId = annotation.value();

        if (!repository.onRequestCommands.contains(requestId)) {
            repository.onRequestCommands.put(requestId, requestFromEntityGateway(requestId));
        }

        if (!repository.onResponseCommands.contains(requestId)) {
            repository.onResponseCommands.put(requestId, notifyModelWithResponse(requestId));
        }

    }


    @NonNull
    private Command<RequestMessage, Void> requestFromEntityGateway(final long requestId) {
        return new Command<RequestMessage, Void>() {
            @Override
            public Void execute(RequestMessage p) {
                RequestMessage requestMessage = p.copyBuilder().id(requestId).build();
                Event event = new Event.Builder(requestId).message(requestMessage).build();
                repository.getEntityGateway().execute(event);
                return null;
            }
        };
    }

    @NonNull
    private Command<ResponseMessage, Void> notifyModelWithResponse(final long requestId) {
        return new Command<ResponseMessage, Void>() {
            @Override
            public Void execute(ResponseMessage p) {
                ResponseMessage newResponseMessage = p.copyBuilder().id(requestId).build();
                Event event = new Event.Builder(requestId).message(newResponseMessage).build();
                repository.notifyCallback(event);
                return null;
            }
        };
    }
}
