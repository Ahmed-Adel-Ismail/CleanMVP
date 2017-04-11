package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/2/2017.
 */

public class PayloadBusy implements Serializable {

    private long packageId;

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }
}
