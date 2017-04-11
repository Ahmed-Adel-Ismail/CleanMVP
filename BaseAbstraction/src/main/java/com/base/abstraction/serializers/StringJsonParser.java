package com.base.abstraction.serializers;

import com.base.abstraction.commands.Command;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * a {@link Command} that parses Json Strings that are stored as {@code String} with escape
 * characters and quotes at there start and end
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class StringJsonParser<T extends Serializable> implements Command<String, T> {

    private Class<T> klass;

    public StringJsonParser(Class<T> klass) {
        this.klass = klass;
    }

    /**
     * @throws UnsupportedOperationException if the passed value is {@code null} or empty
     */
    @Override
    public T execute(String stringJson) throws UnsupportedOperationException {
        if (stringJson == null || stringJson.isEmpty()) {
            throw new UnsupportedOperationException("cannot parse null or empty Strings");
        }
        stringJson = stringJson.substring(0, stringJson.length()).replaceAll("\\\\", "");
        return new Gson().fromJson(stringJson, klass);
    }
}
