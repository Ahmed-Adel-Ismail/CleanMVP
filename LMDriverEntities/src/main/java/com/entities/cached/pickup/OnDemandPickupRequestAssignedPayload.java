package com.entities.cached.pickup;

import com.base.annotations.MockEntity;
import com.entities.mocks.pickup.MockedOnDemandPickupRequestAssignedPayload;

import java.io.Serializable;

/**
 * the payload Object that is received when on-demand-pickup-assigned notification is
 * received
 * <p>
 * Created by Ahmed Adel on 12/19/2016.
 */
@MockEntity(MockedOnDemandPickupRequestAssignedPayload.class)
public class OnDemandPickupRequestAssignedPayload implements Serializable {

    protected long packageId;
    protected long requestId;
    protected String weight;
    protected String address;


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
