package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import com.base.usecases.events.ResponseMessage;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * a class that parses the Generic typed {@code Json} response received in the
 * {@link ResponseMessage} Object
 * <p>
 * Created by Ahmed Adel on 11/1/2016.
 */
class ResponseMessageParser<T extends Serializable> extends AbstractResponseMessageParser {

    private TypeToken<T> typeToken;

    ResponseMessageParser(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    @NonNull
    ResponseMessage.Builder readSuccessfulResponseObject(
            ResponseMessage.Builder builder,
            String responseString) {

        T responseObject = new JsonParser<>(typeToken).execute(responseString);
        builder.content(responseObject);

        return builder;
    }


}
