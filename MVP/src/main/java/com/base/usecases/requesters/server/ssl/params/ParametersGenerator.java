package com.base.usecases.requesters.server.ssl.params;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.logs.Logger;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * a class that generates a {@link ParametersGroup} from a {@link Serializable} Object, it
 * converts it to keys and values like {@code Json}, but instead of {@code Json}, it is returned
 * as a {@link ParametersGroup}
 * <p>
 * Created by Ahmed Adel on 11/7/2016.
 */
public class ParametersGenerator implements Command<Serializable, ParametersGroup> {

    @Override
    public ParametersGroup execute(@NonNull Serializable object) {
        try {
            return createParametersGroup(object);
        } catch (JSONException e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }

    private ParametersGroup createParametersGroup(@NonNull Serializable object)
            throws JSONException {
        ParametersGroup parametersGroup = new ParametersGroup();
        String jsonString = new Gson().toJson(object);
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            addParameter(parametersGroup, jsonObject, keys);
        }
        return parametersGroup;
    }

    private void addParameter(
            ParametersGroup parametersGroup,
            JSONObject jsonObject,
            Iterator<String> keys)
            throws JSONException {

        String key = keys.next();
        Serializable value = (Serializable) jsonObject.get(key);
        parametersGroup.add(new Parameter(key, value));
    }
}
