package com.base.presentation.repos.json;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * a class to convert a Json string to an Object
 * <p>
 * Created by Ahmed Adel on 10/25/2016.
 */
class JsonParser<T> implements Command<String, T> {

    private TypeToken<T> typeToken;

    JsonParser(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    public T execute(String json) {
        T response = null;
        try {
            Type type = typeToken.getType();
            response = new Gson().fromJson(json, type);
        } catch (Throwable e) {
            Logger.getInstance().error(getClass(), "error parsing : " + json);
            Logger.getInstance().exception(e);
        }
        return response;
    }
}
