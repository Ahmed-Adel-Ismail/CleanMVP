package com.base.mocked;

import com.base.cached.ServerImage;

public class MockedServerImage extends ServerImage {



    public MockedServerImage() {
        double randomNumber = (long) (Math.random() * 10);
        if (fileId % 2 == 0) {
            super.fileId = MockedServerImagesGroup.ID_ONE;
            super.uri = "http://xesoftwares.co.in/contactsapi/profile_images/d34b638b93773140eb94d5f03c20237c.jpg";
        } else {
            super.fileId = MockedServerImagesGroup.ID_TWO;
            super.uri = "http://i.imgur.com/DvpvklR.png";
        }
    }
}
