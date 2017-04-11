package com.base.abstraction.serializers;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.NotSavedInPreferencesException;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.Preferences;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Wafaa on 11/21/2016.
 */

public class JsonListSaver<T extends Serializable> extends MappedSerializer implements
        Command<TreeSet<T>, TreeSet<T>>{

    public JsonListSaver(int keyResourceId) {
        super(keyResourceId);
    }


    @Override
    public TreeSet<T> execute(TreeSet<T> list) {
        Preferences preferences = Preferences.getInstance();
        try {
            saveObject(list, preferences);
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
            throw new NotSavedInPreferencesException(keyResourceId);
        }
        return list;
    }

    private void saveObject(@NonNull TreeSet<T> list, Preferences preferences) {
        TreeSet<String> jsonArray = new TreeSet<>();
        for(T t: list){
            String value = new Gson().toJson(t);
            jsonArray.add(value);
        }
        preferences.save(keyResourceId, jsonArray);
    }

}
