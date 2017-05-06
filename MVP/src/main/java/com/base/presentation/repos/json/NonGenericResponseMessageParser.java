package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import com.base.usecases.events.ResponseMessage;

import java.io.Serializable;

/**
 * a class that parses the Non Generic typed {@code Json} response received in the
 * {@link ResponseMessage} Object
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
class NonGenericResponseMessageParser<T extends Serializable>
        extends AbstractResponseMessageParser {

    private Class<T> klass;

    NonGenericResponseMessageParser(Class<T> klass) {
        this.klass = klass;
    }

    @Override
    @NonNull
    ResponseMessage.Builder readSuccessfulResponseObject(
            ResponseMessage.Builder builder,
            String responseString) {
        T responseObject = new NonGenericJsonParser<>(klass).execute(responseString);
        builder.content(responseObject);
        return builder;
    }
}
