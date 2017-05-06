package com.base.abstraction.serializers;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * a class that loads {@link Collections} from {@link android.content.SharedPreferences}
 * Created by Wafaa on 11/21/2016.
 */

public class JsonSetLoader<T extends Serializable> extends MappedSerializer implements
        Command<Class<T>, TreeSet<T>> {


    public JsonSetLoader(int keyResourceId) {
        super(keyResourceId);
    }

    /**
     * load the collection from preferences
     *
     * @param objectClass the class of the collection
     * @return the collection
     * @throws NotSavedInPreferencesException if the Object was not saved in preference
     */


    @Override
    public TreeSet<T> execute(Class<T> objectClass) {
        Preferences preferences = Preferences.getInstance();
        ArrayList<String> values = preferences.load(keyResourceId, new ArrayList<String>());
        if (values.size() != 0) {
            return loadCollection(objectClass, values);
        } else {
            throw new NotSavedInPreferencesException(keyResourceId);
        }
    }

    private TreeSet<T> loadCollection(Class<T> objectClass, ArrayList<String> value) {
        TreeSet<T> list = new TreeSet<>();
        try {
            for (String str : value) {
                T object = null;
                try {
                    object = new Gson().fromJson(str, objectClass);
                } catch (Throwable e) {
                    Logger.getInstance().exception(e);
                }
                list.add(object);
            }
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return list;
    }

}
