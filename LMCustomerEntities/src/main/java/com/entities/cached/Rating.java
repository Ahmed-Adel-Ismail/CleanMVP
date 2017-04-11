package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/29/2016.
 */

public class Rating implements Serializable {


    protected long driverId;
    protected long packageId;



    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }
}
