package com.base.presentation.base.presentation;

import android.view.View;

import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.commands.executors.generators.ExecutorFromFieldsGenerator;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.reflections.Initializer;
import com.base.presentation.annotations.interfaces.ViewModelExecutor;

import java.lang.reflect.Field;

/**
 * a {@link ExecutorFromFieldsGenerator} that scans the {@link ViewModelExecutor} annotations on fields
 * <p>
 * Created by Ahmed Adel on 11/29/2016.
 */
class ViewModelInvalidationHandlerScanner extends
        ExecutorFromFieldsGenerator<ViewModelExecutor, View> {

    private Initializer initializer = new Initializer();

    /**
     * create a {@link ExecutorFromFieldsGenerator} instance
     *
     * @param executor the {@link Executor} that will hold the new Commands
     */
    ViewModelInvalidationHandlerScanner(Executor<View> executor) {
        super(executor, ViewModelExecutor.class);
    }

    @SuppressWarnings("unchecked")
    protected void processFieldAnnotation(
            Executor<View> executor,
            Field field,
            ViewModelExecutor annotation) {
        try {
            Class<?> clss = field.getType();

            ViewModelInvalidationHandler<?> exec =
                    (ViewModelInvalidationHandler<?>) initializer.execute(clss);

            exec.initialize((ViewModel) getClientObject());
            field.setAccessible(true);
            field.set(getClientObject(), exec);
            executor.putAll(exec);
        } catch (Throwable e) {
            new TestException().execute(e);
        }
    }
}
