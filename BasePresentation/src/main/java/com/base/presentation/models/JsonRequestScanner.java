package com.base.presentation.models;

import com.base.abstraction.annotations.interfaces.Retry;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.reflections.Initializer;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.references.Entity;
import com.base.presentation.repos.base.NullRepository;
import com.base.presentation.repos.base.Repository;

import java.lang.reflect.Field;

/**
 * a class tha scans {@link JsonRequest} annotation
 * <p>
 * Created by Ahmed Adel on 2/7/2017.
 */
@SuppressWarnings("unchecked")
class JsonRequestScanner extends FieldAnnotationScanner<JsonRequest> {

    private Model model;

    JsonRequestScanner() {
        super(JsonRequest.class);
    }

    @Override
    public Void execute(Object annotatedObject) {
        if (annotatedObject instanceof Model) {
            updateModelVariableAndExecuteThenClear(annotatedObject);
        } else {
            throw new UnsupportedOperationException("must pass " + Model.class.getSimpleName() +
                    " to execute() method");
        }
        return null;
    }

    private void updateModelVariableAndExecuteThenClear(Object annotatedObject) {
        this.model = (Model) annotatedObject;
        super.execute(annotatedObject);
        this.model = null;
    }

    @Override
    protected void onAnnotationFound(Field element, JsonRequest annotation) {
        try {
            updateEntityFromJsonRequestAnnotation(element, annotation);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
    }


    private void updateEntityFromJsonRequestAnnotation(Field element, JsonRequest annotation)
            throws IllegalAccessException {

        Entity entity = initializeEntityIfNull(element, (Entity) element.get(model));

        long requestId = annotation.value();
        updateEntityValues(element, requestId, entity);
        updateModelValues(annotation, requestId, entity);
    }

    private Entity initializeEntityIfNull(Field element, Entity entity)
            throws IllegalAccessException {

        if (entity == null) {
            entity = (Entity) new Initializer().execute(element.getType());
            element.set(model, entity);
        }
        return entity;
    }


    private void updateEntityValues(Field element, long requestId, Entity entity) {
        entity.initialize(requestId, model);
        if (element.isAnnotationPresent(Retry.class)) {
            updateEntityFromRetryAnnotation(entity, element.getAnnotation(Retry.class));
        }
    }


    private void updateEntityFromRetryAnnotation(Entity entity, Retry annotation) {
        entity.setRetryPolicy(true);
        entity.setRetryIntervalMillis(annotation.value());
    }

    private void updateModelValues(JsonRequest annotation, long requestId, Entity entity) {
        model.onViewsUpdatedCommands.put(requestId, entity.getRequesterCommand());
        model.onRepositoryUpdatedCommands.put(requestId, entity.getResponderCommand());
        model.entities.add(entity);
        Class<? extends Repository> repoClass = annotation.repository();
        if (!NullRepository.class.equals(repoClass)) {
            model.repositories.add(new Initializer<Repository>().execute(repoClass));
        }
    }

}
