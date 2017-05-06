package com.base.presentation.repos.json;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * a class similar to {@link JsonResponse} but for non-generic classes
 * <p>
 * Created by Ahmed Adel on 12/24/2016.
 */
public class NonGenericJsonResponse<T extends Serializable> extends AbstractJsonResponse<T> {

    private Class<T> klass;


    public void setClass(Class<T> klass) {
        this.klass = klass;
    }

    @NonNull
    @Override
    AbstractResponseMessageParser createResponseMessageParser() {
        return new NonGenericResponseMessageParser<>(klass);
    }
}
