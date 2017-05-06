package com.base.presentation.base.abstracts.features;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.annotations.interfaces.Source;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.Property;
import com.base.abstraction.system.AppResources;

import java.lang.reflect.Field;

/**
 * a Class that scans the {@link Load} annotation and do loads it's values from {@link Intent}
 * if it's source is set to {@link Source#INTENT}
 * <p>
 * Created by Ahmed Adel on 2/1/2017.
 */
class IntentLoader implements Command<Intent, IntentLoader> {

    private Object annotatedObject;

    IntentLoader(Object annotatedObject) {
        this.annotatedObject = annotatedObject;
    }

    @Override
    public IntentLoader execute(@NonNull Intent intent) {
        loadFieldsFromIntent(intent);
        return this;
    }


    private void loadFieldsFromIntent(final Intent intent) {
        new FieldAnnotationScanner<Load>(Load.class) {
            @Override
            protected void onAnnotationFound(Field element, Load annotation) {
                if (annotation.source() == Source.INTENT) {
                    try {
                        doLoadFieldsFromIntent(element, annotation.value()[0], intent);
                    } catch (Throwable e) {
                        Logger.getInstance().exception(e);
                    }
                }
            }
        }.execute(annotatedObject);
    }

    @SuppressWarnings("unchecked")
    private void doLoadFieldsFromIntent(Field element, long keyResource, Intent intent)
            throws IllegalAccessException {

        String key = AppResources.string((int) keyResource);
        Object value = intent.getSerializableExtra(key);

        if (value == null) {
            Logger.getInstance().error(getClass(), "no value stored for Intent key : " + key);
        }

        if (Property.class.isAssignableFrom(element.getType())) {
            Property p = (Property) element.get(annotatedObject);
            if (p != null) p.set(value);
        } else {
            element.set(annotatedObject, value);
        }
    }


}
