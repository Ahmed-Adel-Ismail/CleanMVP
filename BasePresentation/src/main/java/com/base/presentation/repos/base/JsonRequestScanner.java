package com.base.presentation.repos.base;

import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.logs.Logger;
import com.base.presentation.repos.json.JsonRequest;
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
    protected void onAnnotationFound(
            Field element,
            com.base.presentation.annotations.interfaces.JsonRequest annotation) {

        if (repository.jsonRequest == null) {
            repository.jsonRequest = new JsonRequest(repository.getEntityGateway());
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
            repository.onResponseCommands.put(requestId, response);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
    }
}
