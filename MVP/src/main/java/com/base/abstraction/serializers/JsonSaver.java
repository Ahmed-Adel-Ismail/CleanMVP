package com.base.abstraction.serializers;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * a class that saves {@link Serializable} Objects to {@link android.content.SharedPreferences},
 * note that this class is intended for Objects that are used as Json Objects, not primitive
 * types or {@code Strings}
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 *
 * @see JsonLoader
 */
public class JsonSaver<T extends Serializable> extends MappedSerializer implements Command<T, T> {


    public JsonSaver(int keyResourceId) {
        super(keyResourceId);
    }

    /**
     * save an Object to preferences
     *
     * @param object the Object to be saved
     * @return the same Object after saving
     * @throws NotSavedInPreferencesException if the saving operation failed
     */
    @Override
    public T execute(@NonNull T object) {
        Preferences preferences = Preferences.getInstance();
        try {
            saveObject(object, preferences);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            throw new NotSavedInPreferencesException(keyResourceId);
        }
        return object;
    }

    private void saveObject(@NonNull T object, Preferences preferences) {
        String value = new Gson().toJson(object);
        preferences.save(keyResourceId, value);
    }
}
