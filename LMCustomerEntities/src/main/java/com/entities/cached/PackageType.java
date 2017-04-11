package com.entities.cached;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 6/2/2016.
 */
public class PackageType implements Serializable {

    private long branchId;
    private long companyId;
    private String description;
    private long expectedWeight;
    private long isactive;
    private String packageDimension;
    private long packageTypeId;
    private String packageType;

    private List<PackageType> packageTypeList;


    public long getExpectedWeight() {
        return expectedWeight;
    }

    public String getPackageType() {
        return packageType;
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getBranchId() {
        return branchId;
    }

    public long getIsactive() {
        return isactive;
    }

    public String getDescription() {
        return description;
    }

    public void setPackageTypeList(List<PackageType> packageTypeList) {
        this.packageTypeList = packageTypeList;
    }

    public long getPackageTypeId() {
        return packageTypeId;
    }

    public String getPackageDimension() {
        return packageDimension;
    }

    public void setPackageTypes(List<PackageType> packageTypes) {
        packageTypeList = packageTypes;
    }

    public List<PackageType> getPackageTypeList() {
        return packageTypeList;
    }
}
