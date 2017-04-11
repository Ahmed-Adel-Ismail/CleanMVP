package com.entities.mocks.pakage;

import com.entities.cached.pakage.PackageType;
import com.base.cached.AvailablePackageTypes;
import com.entities.cached.pakage.PackageTypesGroup;

public class MockedPackageTypesGroup extends PackageTypesGroup {

    MockedPackageTypesGroup() {

        PackageType p = new PackageType();
        p.setPackageTypeId(AvailablePackageTypes.BOX.id);
        p.setPackageType("Box");
        p.setExpectedWeight(25);
        p.setPackageDimension("10 * 10 * 10");
        add(p);

        p = new PackageType();
        p.setPackageTypeId(AvailablePackageTypes.DOCUMENT.id);
        p.setPackageType("Document");
        p.setExpectedWeight(10);
        p.setPackageDimension("5 * 5 * 5");
        add(p);

    }

}

