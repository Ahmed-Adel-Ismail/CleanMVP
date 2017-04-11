package com.entities.mocks.pakage;

import com.entities.cached.pakage.PackageLabel;


public class MockedPackageLabel extends PackageLabel {

    public MockedPackageLabel() {
        super.packageLabelId = (long) (Math.random() * 99);
        super.label = "[" + packageLabelId + "] name";
    }
}
