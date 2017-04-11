package com.base.mocked;

import com.base.cached.ServerImagesGroup;

public class MockedServerImagesGroup extends ServerImagesGroup {

    public final static long ID_ONE = 1;
    public final static long ID_TWO = 2;

    public MockedServerImagesGroup() {
        MockedServerImage image = new MockedServerImage();
        image.setFileId(ID_ONE);
        put(0, image);

        image = new MockedServerImage();
        image.setFileId(ID_TWO);
        put(1, image);
    }
}
