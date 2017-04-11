package com.entities.requesters;

import java.io.Serializable;

/**
 * Created by Wafaa on 8/8/2016.
 */
public class PackageTypeBody  implements Serializable{

    private long companyId = 0;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
