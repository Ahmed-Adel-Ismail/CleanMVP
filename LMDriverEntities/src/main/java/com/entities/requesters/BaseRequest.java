package com.entities.requesters;

import java.io.Serializable;

/**
 * the base class for all request Objects in the application
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
public class BaseRequest implements Serializable {

    private long packageId;

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }
}
