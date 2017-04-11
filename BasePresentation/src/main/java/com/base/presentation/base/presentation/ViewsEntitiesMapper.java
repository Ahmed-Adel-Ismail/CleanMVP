package com.base.presentation.base.presentation;

import android.support.annotation.NonNull;

import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Initializer;
import com.base.abstraction.annotations.scanners.MethodAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.base.presentation.annotations.interfaces.EditTextWatcher;
import com.base.presentation.annotations.interfaces.OnResponse;
import com.base.presentation.models.Model;
import com.base.presentation.references.Entity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * a Class that maps every view-id to the {@link Entity} that manages it's request-id, and returns
 * a {@link ViewsEntities} Object that holds the view-id as the key, and a group of {@link Entity}
 * instances declared in the {@link Model} as the value ...
 * the returned {@link ViewsEntities} can be {@code empty} if there are no mapped
 * {@link Entity} members
 * <p>
 * Created by Ahmed Adel on 1/30/2017.
 */
class ViewsEntitiesMapper implements Command<ViewModel, ViewsEntities> {

    @Override
    public ViewsEntities execute(final ViewModel viewModel) {
        final ViewsEntities viewsEntities = new ViewsEntities();
        try {
            createAnnotationScanner(viewModel, viewsEntities).execute(viewModel);
        } catch (UnsupportedOperationException e) {
            Logger.getInstance().exception(e);
        }
        return viewsEntities;
    }

    @NonNull
    private MethodAnnotationScanner<OnResponse> createAnnotationScanner(
            final ViewModel viewModel,
            final ViewsEntities viewsEntities) {

        return new MethodAnnotationScanner<OnResponse>(OnResponse.class) {
            final HashMap<Long, Entity<?>> modelEntities = new HashMap<>();

            @Override
            protected void onAnnotationFound(Method method, OnResponse annotation) {

                Params p = new Params();
                p.method = method;
                p.viewModel = viewModel;
                p.modelEntities = modelEntities;
                p.viewsEntities = viewsEntities;

                if (p.modelEntities.isEmpty()) {
                    p.modelEntities.putAll(scanEntitiesFromModelOrThrowException(p.viewModel));
                }

                for (long requestId : annotation.value()) {
                    startMappingEntityToViewsIds(p, requestId);
                }
            }
        };
    }

    private HashMap<Long, Entity<?>> scanEntitiesFromModelOrThrowException(ViewModel viewModel) {

        HashMap<Long, Entity<?>> entities = new HashMap<>();
        List<Entity<?>> modelEntities = viewModel.getFeature().getModel().getEntities();

        for (Entity<?> entity : modelEntities) {
            entities.put(entity.getRequestId(), entity);
        }

        if (entities.isEmpty()) {
            throw new UnsupportedOperationException("no entities declared in the model");
        }

        return entities;
    }

    private void startMappingEntityToViewsIds(Params p, long requestId) {
        Entity<?> entity = p.modelEntities.get(requestId);
        if (entity != null) {
            mapEntityToViewsIds(p, entity);
        } else {
            logNoEntityFoundError(p.viewModel, requestId);
        }
    }

    private void mapEntityToViewsIds(Params p, Entity<?> entity) {

        int viewsEntitiesOriginalSize = p.viewsEntities.size();

        if (p.method.isAnnotationPresent(Initializer.class)) {
            p.viewsEntities.putForKeys(initializerViewsIds(p.method), entity);
        }

        if (p.method.isAnnotationPresent(Executable.class)) {
            p.viewsEntities.putForKeys(executableViewsIds(p.method), entity);
        }

        if (p.method.isAnnotationPresent(EditTextWatcher.class)) {
            p.viewsEntities.putForKeys(editTextWatcherViewsIds(p.method), entity);
        }

        if (viewsEntitiesOriginalSize == p.viewsEntities.size()) {
            Logger.getInstance().error(getClass(), "no annotation found to supply view Ids");
        }
    }

    private long[] initializerViewsIds(Method method) {
        return method.getAnnotation(Initializer.class).value();
    }

    private long[] executableViewsIds(Method method) {
        return method.getAnnotation(Executable.class).value();
    }

    private long[] editTextWatcherViewsIds(Method method) {
        return method.getAnnotation(EditTextWatcher.class).value();
    }


    private void logNoEntityFoundError(ViewModel viewModel, long requestId) {
        Logger.getInstance().error(getClass(), "no Entity found for @" +
                OnResponse.class.getSimpleName() + " request Id : "
                + AppResources.resourceEntryName((int) requestId) +
                "in Model : " + viewModel.getFeature().getModel());
    }


    private static class Params {
        Method method;
        ViewModel viewModel;
        HashMap<Long, Entity<?>> modelEntities;
        ViewsEntities viewsEntities;
    }
}
