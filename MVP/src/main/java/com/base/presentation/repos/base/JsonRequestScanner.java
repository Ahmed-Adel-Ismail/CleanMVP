package com.base.presentation.repos.base;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.reflections.Initializer;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.repos.json.NonGenericJsonResponse;

import java.lang.reflect.Field;

class JsonRequestScanner extends FieldAnnotationScanner<com.base.presentation.annotations.interfaces.JsonRequest> {

    private Repository repository;

    JsonRequestScanner() {
        super(com.base.presentation.annotations.interfaces.JsonRequest.class);
    }

    @Override
    public Void execute(Object annotatedObject) {
        repository = (Repository) annotatedObject;
        super.execute(annotatedObject);
        repository = null;
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onAnnotationFound(Field element, JsonRequest annotation) {

        if (repository.jsonRequest == null) {
            repository.jsonRequest = createJsonRequest();
        }

        long requestId = annotation.value();

        if (annotation.concurrent()) {
            repository.getEntityGateway().addConcurrentRequestId(requestId);
        }

        repository.onRequestCommands.put(requestId, repository.jsonRequest);

        try {
            NonGenericJsonResponse response = new NonGenericJsonResponse();
            response.initialize(repository);
            response.setClass(element.getType());
            response.setErrorParser(errorParser(annotation));
            repository.onResponseCommands.put(requestId, response);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
    }

    @NonNull
    private com.base.presentation.repos.json.JsonRequest createJsonRequest() {
        return new com.base.presentation.repos.json.JsonRequest(repository.getEntityGateway());
    }

    private Command<String, ?> errorParser(JsonRequest annotation) {
        return new Initializer<Command<String, ?>>().execute(annotation.errorParser());
    }

}
