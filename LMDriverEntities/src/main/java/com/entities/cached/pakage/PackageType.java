package com.entities.cached.pakage;

import com.base.annotations.MockEntity;
import com.base.cached.AvailablePackageTypes;
import com.entities.mocks.pakage.MockedPackageType;

import java.io.Serializable;

/**
 * the type of a package .. which will describe it's dimensions, expected weight, etc..
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedPackageType.class)
public class PackageType implements Serializable, Comparable<PackageType> {

    protected long packageTypeId;
    protected String packageType;
    protected String packageDimension;
    protected double expectedWeight;

    public double getExpectedWeight() {
        return expectedWeight;
    }

    public void setExpectedWeight(double expectedWeight) {
        this.expectedWeight = expectedWeight;
    }

    public String getPackageDimension() {
        return packageDimension;
    }

    public void setPackageDimension(String packageDimension) {
        this.packageDimension = packageDimension;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public long getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    @Override
    public int hashCode() {
        return (int) packageTypeId % 2;
    }

    /**
     * get the {@link AvailablePackageTypes} of this {@link PackageType}
     *
     * @return the {@link AvailablePackageTypes} based on {@link #getPackageTypeId()} value
     */
    public AvailablePackageTypes getPackageTypeIdEnum() {
        return AvailablePackageTypes.FACTORY.getType(packageTypeId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof PackageType
                && ((PackageType) obj).packageTypeId == packageTypeId;
    }

    @Override
    public int compareTo(PackageType o) {
        if (o == null || o.packageType == null) {
            return -1;
        } else if (packageType == null) {
            return 1;
        } else {
            return packageType.compareTo(o.packageType);
        }
    }
}
