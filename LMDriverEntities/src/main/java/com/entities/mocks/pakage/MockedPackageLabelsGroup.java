package com.entities.mocks.pakage;

import com.entities.cached.pakage.PackageLabel;
import com.entities.cached.pakage.PackageLabelsGroup;


public class MockedPackageLabelsGroup extends PackageLabelsGroup {

    public MockedPackageLabelsGroup() {

        PackageLabel label = new PackageLabel();
        label.setPackageLabelId(1);
        label.setLabel("Handle with Care");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(2);
        label.setLabel("Glass");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(3);
        label.setLabel("Use No Hooks");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(4);
        label.setLabel("This Side Up");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(5);
        label.setLabel("Fragile");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(6);
        label.setLabel("Keep In Cool Place");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(7);
        label.setLabel("Keep Dry");
        add(label);

        label = new PackageLabel();
        label.setPackageLabelId(8);
        label.setLabel("Opened Here");
        add(label);


    }
}
