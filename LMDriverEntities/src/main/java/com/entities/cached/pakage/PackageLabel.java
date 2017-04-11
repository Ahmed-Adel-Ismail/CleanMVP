package com.entities.cached.pakage;

import com.base.annotations.MockEntity;
import com.entities.mocks.pakage.MockedPackageLabel;

import java.io.Serializable;

/**
 * the Package Label Object (like Fragile for example)
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPackageLabel.class)
public class PackageLabel implements Serializable {

    protected long packageLabelId;
    protected String label;

    public long getPackageLabelId() {
        return packageLabelId;
    }

    public void setPackageLabelId(long packageLabelId) {
        this.packageLabelId = packageLabelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
