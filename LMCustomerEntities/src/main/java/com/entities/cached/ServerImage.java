package com.entities.cached;

import java.io.Serializable;

/**
 * an Object that holds the info for the image on static content server
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */

public class ServerImage implements Serializable {

    public static final String SIZE_SMALL = "small";
    public static final String SIZE_MEDIUM = "medium";
    public static final String SIZE_LARGE = "large";

    protected long id;
    protected String uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
