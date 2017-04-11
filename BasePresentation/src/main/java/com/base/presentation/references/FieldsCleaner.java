package com.base.presentation.references;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.TestException;
import com.base.abstraction.interfaces.Clearable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * a class that is responsible for clearing member variables, either by setting normal variables
 * to {@code null}, or by invoking {@link Property#clear()} on {@link Property} variables,
 * you should execute this {@link Command} at the very end of your Object's life-cycle
 * <p>
 * Created by Ahmed Adel on 2/14/2017.
 */
public class FieldsCleaner implements Command<Object, Void> {

    private boolean clearFirst;

    public FieldsCleaner() {
    }

    /**
     * initialize {@link FieldsCleaner}
     *
     * @param clearFirst pass {@code true} for this class to invoke {@link Clearable#clear()} if
     *                   it found that a field is of type {@link Clearable}
     */
    public FieldsCleaner(boolean clearFirst) {
        this.clearFirst = clearFirst;
    }

    @Override
    public Void execute(@NonNull Object hostObject) {
        Field[] fields = hostObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            clearField(hostObject, field);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void clearField(Object hostObject, Field field) {
        try {
            field.setAccessible(true);
            if (Property.class.isAssignableFrom(field.getType())) {
                clearProperty(hostObject, field);
            } else if (clearFirst) {
                invokeClear(hostObject, field);
            }
            updateObjectToNull(hostObject, field);

        } catch (Throwable e) {
            new TestException().execute(e);
        }
    }

    private void clearProperty(Object hostObject, Field field) throws IllegalAccessException {
        Property p = (Property) field.get(hostObject);
        if (p != null) p.clear();
    }


    private void invokeClear(Object hostObject, Field field) throws IllegalAccessException {
        if (Clearable.class.isAssignableFrom(field.getType())) {
            Clearable c = (Clearable) field.get(hostObject);
            if (c != null) c.clear();
        }
    }

    private void updateObjectToNull(Object hostObject, Field field) throws IllegalAccessException {
        if (Object.class.isAssignableFrom(field.getType())
                && !Modifier.isFinal(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers())) {
            field.set(hostObject, null);
        }

    }

}
