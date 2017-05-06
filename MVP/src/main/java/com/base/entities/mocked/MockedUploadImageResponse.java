package com.base.entities.mocked;

import com.base.entities.cached.UploadImageResponse;

/**
 * a mocked object of the upload image response
 * <p>
 * Created by Ahmed Adel on 2/13/2017.
 */
public class MockedUploadImageResponse extends UploadImageResponse {

    public MockedUploadImageResponse() {
        fileId = MockedImagesIds.fromServer105().getRandomId();
    }
}
