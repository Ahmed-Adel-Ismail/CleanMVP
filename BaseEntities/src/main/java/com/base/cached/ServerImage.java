package com.base.cached;

import com.base.annotations.MockEntity;
import com.base.mocked.MockedServerImage;

import java.io.Serializable;

/**
 * an Object that holds the info for the image on static content server
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedServerImage.class)
public class ServerImage implements Serializable {

    public static final String SIZE_SMALL = "small";
    public static final String SIZE_MEDIUM = "medium";
    public static final String SIZE_LARGE = "large";

    public static final long FLAG_SELECTED = 8000;

    protected long fileId;
    protected String uri;

    private transient long flags;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean hasFileId() {
        return fileId != 0;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public void resetFlags() {
        flags = 0;
    }

    @Override
    public int hashCode() {
        return (uri != null) ? uri.length() : (int) fileId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof ServerImage
                && hasSameData((ServerImage) obj);
    }

    private boolean hasSameData(ServerImage obj) {
        boolean result = fileId == obj.fileId;
        if (result && uri != null) {
            return uri.equals(obj.uri);
        }
        return result;
    }

    @Override
    public String toString() {
        return "{[fileId=" + fileId + "], [uri=" + uri + "], [flags=" + flags + "]}";
    }
}
