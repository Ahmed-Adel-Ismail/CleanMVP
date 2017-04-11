package com.entities.mocks.pickup;

import com.entities.cached.pickup.PickupOrdersGroup;

public class MockedPickupOrdersGroup extends PickupOrdersGroup {

    public MockedPickupOrdersGroup() {
        int total = (int) (Math.random() * 20);
        while (--total > 0) {
            add(new MockedPickupOrder());
        }
    }
}
