package com.entities.requesters;

import com.base.annotations.MockEntity;

import java.io.Serializable;

/**
 * the Object that holds the ids necessary for pickup-process to complete
 * <p>
 * Created by Ahmed Adel on 1/31/2017.
 */
@MockEntity
public class PickupProcessId implements Serializable {

    private long packageId;
    private long requestId;

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
