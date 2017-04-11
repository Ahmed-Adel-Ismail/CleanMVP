package com.entities.mocks.pickup;

import com.entities.cached.pickup.OnDemandPickupRequestAssignedPayload;

public class MockedOnDemandPickupRequestAssignedPayload
        extends OnDemandPickupRequestAssignedPayload {

    public MockedOnDemandPickupRequestAssignedPayload() {
        super.requestId = (long) (Math.random() * 5000);
        super.packageId = (long) (Math.random() * 5000);
        super.weight = "Box (10kg)";
        super.address = "8 Nabatat st., 13 District, West Somid, 6th of October, Giza, Egypt";
    }
}
