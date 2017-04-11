package com.entities.mocks.pakage;

import com.entities.cached.pakage.PackageType;
import com.base.cached.AvailablePackageTypes;

public class MockedPackageType extends PackageType {

    public MockedPackageType() {
        super.packageTypeId = AvailablePackageTypes.values()
                [(int) (Math.random() * AvailablePackageTypes.values().length)].id;
        super.packageType = "[" + packageTypeId + "] type";
        super.packageDimension = "[" + packageTypeId + "] dimensions";
        super.expectedWeight = (int) (Math.random() * 25);
    }
}
