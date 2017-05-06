package com.base.entities.requesters;

import java.io.Serializable;

/**
 * An Object used to upload images to Static content server
 * <p/>
 * Created by Ahmed Adel on 9/28/2016.
 */
public class UploadImageParam implements Serializable {

    private String extension;
    private String name;
    private String base64ByteArray;

    public String getBase64ByteArray() {
        return base64ByteArray;
    }

    public void setBase64ByteArray(String base64ByteArray) {
        this.base64ByteArray = base64ByteArray;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
