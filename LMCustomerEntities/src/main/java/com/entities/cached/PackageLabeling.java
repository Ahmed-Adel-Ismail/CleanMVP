package com.entities.cached;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 6/7/2016.
 */
public class PackageLabeling implements Serializable {

    private long componentId;
    private String name;
    private long companyId;
    private long branchId;
    private long isactive;
    private long isdeleted;
    private String description;
    private long lookupId;
    private String lookupValue;
    private long lookupCompanyId;
    private long lookupBranchId;
    private int lookupIsactive;
    private int lookupIsdeleted;
    private String lookupDescription;
    private boolean checked;
    private List<PackageLabeling> packageLabelingList;

    public long getComponentId() {
        return componentId;
    }

    public String getName() {
        return name;
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

    public long getIsdeleted() {
        return isdeleted;
    }

    public String getDescription() {
        return description;
    }

    public long getLookupId() {
        return lookupId;
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public long getLookupCompanyId() {
        return lookupCompanyId;
    }

    public long getLookupBranchId() {
        return lookupBranchId;
    }

    public int getLookupIsactive() {
        return lookupIsactive;
    }

    public int getLookupIsdeleted() {
        return lookupIsdeleted;
    }

    public String getLookupDescription() {
        return lookupDescription;
    }

    public List<PackageLabeling> getPackageLabelingList() {
        return packageLabelingList;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setPackageLabelingList(List<PackageLabeling> packageLabelingList) {
        this.packageLabelingList = packageLabelingList;
    }

}
