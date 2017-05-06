package com.base.abstraction.system.readers;

import android.content.Intent;
import android.content.IntentFilter;

import com.base.abstraction.annotations.interfaces.EventBusSubscriber;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.reflections.Initializer;
import com.base.abstraction.system.App;
import com.base.abstraction.system.AppResources;
import com.base.abstraction.system.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * a class that reads {@link EventBusSubscriber} annotation and setup the broadcast receiver
 * for handling the mentioned actions in those executors
 * <p>
 * Created by Ahmed Adel on 2/19/2017.
 */
class BroadcastsAnnotationReader implements Command<App, Void> {

    @Override
    public Void execute(final App app) {
        final IntentFilter intentFilter = new IntentFilter();
        final List<Executor<Intent>> executors = new ArrayList<>();

        new FieldAnnotationScanner<EventBusSubscriber>(EventBusSubscriber.class) {
            @Override
            protected void onAnnotationFound(Field field, EventBusSubscriber annotation) {
                try {
                    processAnnotation(executors, app, field, intentFilter);
                } catch (Throwable e) {
                    new TestException().execute(e);
                }
            }
        }.execute(app);

        if (!executors.isEmpty()) {
            app.registerReceiver(new EventBus(executors), intentFilter);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void processAnnotation(
            List<Executor<Intent>> executors,
            App app,
            Field field,
            IntentFilter intentFilter)
            throws
            IllegalAccessException,
            UnsupportedOperationException {

        Executor<Intent> executor = (Executor) new Initializer<>().execute(field.getType());
        Set<Long> actionIds = executor.keySet();

        if (actionIds.isEmpty()) {
            throwUnsupportedOperationException();
        }

        field.set(app, executor);

        for (Long id : actionIds) {
            intentFilter.addAction(AppResources.string((int) ((long) id)));
        }

        executors.add(executor);
    }

    private void throwUnsupportedOperationException() {
        throw new UnsupportedOperationException("@" + EventBusSubscriber.class.getSimpleName()
                + " must have at least on command to execute mapped to a " +
                "String resource that is a Broadcast ACTION");
    }
}
