package com.base.abstraction.serializers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * a class that loads {@link Serializable} Objects from {@link android.content.SharedPreferences},
 * note that this class is intended for Objects that are used as Json Objects, not primitive
 * types or {@code Strings}
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 *
 * @see JsonSaver
 */
public class JsonLoader<T extends Serializable> extends MappedSerializer implements
        Command<Class<T>, T> {

    public JsonLoader(int keyResourceId) {
        super(keyResourceId);
    }

    /**
     * loadAnnotatedElements the Object from preferences
     *
     * @param objectClass the class of the Object
     * @return the Object
     * @throws NotSavedInPreferencesException if the Object was not saved in preference
     */
    @Override
    public T execute(Class<T> objectClass) {
        Preferences preferences = Preferences.getInstance();
        String value = preferences.load(keyResourceId, NO_VALUE);
        if (!NO_VALUE.equals(value)) {
            return loadObject(objectClass, value);
        } else {
            throw new NotSavedInPreferencesException(keyResourceId);
        }
    }

    private T loadObject(Class<T> objectClass, String value) {
        T object = null;
        try {
            object = new Gson().fromJson(value, objectClass);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return object;
    }


}
