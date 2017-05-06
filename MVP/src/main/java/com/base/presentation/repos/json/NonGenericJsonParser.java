package com.base.presentation.repos.json;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.google.gson.Gson;

/**
 * a class similar to {@link JsonParser} but parses Non-Generic types
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
class NonGenericJsonParser<T> implements Command<String, T> {

    private Class<T> klass;

    NonGenericJsonParser(Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public T execute(String json) {
        T response = null;
        try {
            response = new Gson().fromJson(json, klass);
        } catch (Throwable e) {
            Logger.getInstance().error(getClass(), "error parsing : " + json);
            Logger.getInstance().exception(e);
        }
        return response;
    }
}
