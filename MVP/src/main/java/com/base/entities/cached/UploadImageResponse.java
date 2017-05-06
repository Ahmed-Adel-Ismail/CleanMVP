package com.base.entities.cached;

import com.base.entities.annotations.MockEntity;
import com.base.entities.mocked.MockedUploadImageResponse;

import java.io.Serializable;

/**
 * A class for handeling the Upload image response
 * <p/>
 * Created by Ahmed Adel on 9/28/2016.
 */
@MockEntity(MockedUploadImageResponse.class)
public class UploadImageResponse implements Serializable {

    protected long fileId;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }
}
