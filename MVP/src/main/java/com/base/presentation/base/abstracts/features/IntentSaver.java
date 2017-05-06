package com.base.presentation.base.abstracts.features;

import android.content.Intent;

import com.base.abstraction.annotations.interfaces.Save;
import com.base.abstraction.annotations.interfaces.Source;
import com.base.abstraction.annotations.scanners.FieldAnnotationScanner;
import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.base.presentation.references.Property;
import com.base.abstraction.system.AppResources;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * a class that scans the {@link Save} annotation and save it's values to the {@link Intent} if
 * it's source is set to {@link Source#INTENT}
 * <p>
 * Created by Ahmed Adel on 2/5/2017.
 */
class IntentSaver implements Command<Intent, IntentSaver> {

    private Object annotatedObject;

    IntentSaver(Object annotatedObject) {
        this.annotatedObject = annotatedObject;
    }

    @Override
    public IntentSaver execute(Intent intent) {
        saveFieldsToIntent(intent);
        return this;
    }

    private void saveFieldsToIntent(final Intent intent) {
        new FieldAnnotationScanner<Save>(Save.class) {
            @Override
            protected void onAnnotationFound(Field element, Save annotation) {
                if (annotation.source() == Source.INTENT) {
                    try {
                        doSaveFieldsToIntent(element, annotation.value()[0], intent);
                    } catch (Throwable e) {
                        Logger.getInstance().exception(e);
                    }
                }
            }
        }.execute(annotatedObject);
    }

    @SuppressWarnings("unchecked")
    private void doSaveFieldsToIntent(Field element, long keyResource, Intent intent)
            throws IllegalAccessException {

        String key = AppResources.string((int) keyResource);
        Object value;

        value = element.get(annotatedObject);
        if (value != null && value instanceof Property) {
            value = ((Property) value).get();
        }

        if (value == null) {
            Logger.getInstance().error(getClass(), "no value to be stored for Intent key : " + key);
        } else if (!(value instanceof Serializable)) {
            throw new UnsupportedOperationException("non-serializable values are not supported");
        } else {
            intent.putExtra(key, (Serializable) value);
        }
    }

}
