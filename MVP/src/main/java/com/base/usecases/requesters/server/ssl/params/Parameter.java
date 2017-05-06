package com.base.usecases.requesters.server.ssl.params;

import com.base.abstraction.system.AppResources;

import java.io.Serializable;

/**
 * a Class that represents a URL parameter
 * <p>
 * Created by Wafaa on 10/20/2016.
 */
public class Parameter implements Serializable {

    private Serializable key;
    private Serializable value;

    public Parameter(int keyResourceId, Serializable value) {
        this.key = AppResources.string(keyResourceId);
        this.value = value;
    }

    Parameter(String key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + key + "=" + value + "]";
    }
}
