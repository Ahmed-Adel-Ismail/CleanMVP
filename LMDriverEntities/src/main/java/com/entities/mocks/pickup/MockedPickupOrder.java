package com.entities.mocks.pickup;

import com.entities.cached.pickup.PickupOrder;

public class MockedPickupOrder extends PickupOrder {

    public MockedPickupOrder() {
        super.id = (long) (Math.random() * 5000);
    }
}
