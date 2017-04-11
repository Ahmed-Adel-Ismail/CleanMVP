package com.base.abstraction.serializers;

import android.support.annotation.NonNull;

import com.base.abstraction.system.Preferences;

import java.io.Serializable;

/**
 * a class that is used to loadAnnotatedElements / save values from preferences
 * <p>
 * Created by Ahmed Adel on 11/16/2016.
 */
public abstract class ValueSerializer<T extends Serializable> extends MappedSerializer {

    private T defaultValue;
    private boolean lastOperationSuccessful;

    protected ValueSerializer(int keyResourceId, @NonNull T defaultValue) {
        super(keyResourceId);
        this.defaultValue = defaultValue;

    }

    /**
     * save value to preferences
     *
     * @param value the value to be saved
     * @return the value saved, or {@code null} if the operation failed
     */
    public final T save(T value) {
        lastOperationSuccessful = Preferences.getInstance().save(keyResourceId, value);
        return (lastOperationSuccessful) ? value : null;
    }

    /**
     * loadAnnotatedElements value from preferences
     *
     * @return the value found, or {@code null} if not found
     */
    public final T load() {
        T result = Preferences.getInstance().load(keyResourceId, defaultValue);
        lastOperationSuccessful = !defaultValue.equals(result);
        return (lastOperationSuccessful) ? result : null;
    }

    /**
     * check weather the last operation was successful or not
     *
     * @return {@code true} if the last operation was successful, the default return value is
     * {@code false}
     */
    public final boolean isLastOperationSuccessful() {
        return lastOperationSuccessful;
    }
}
