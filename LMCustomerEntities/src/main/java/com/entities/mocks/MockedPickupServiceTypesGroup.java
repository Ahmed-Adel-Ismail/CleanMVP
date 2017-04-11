package com.entities.mocks;

import com.entities.cached.PickupServiceGroup;

import java.math.BigDecimal;

/**
 * Created by Wafaa on 12/28/2016.
 */

public class MockedPickupServiceTypesGroup extends PickupServiceGroup {

    public MockedPickupServiceTypesGroup() {
        add(new MockedPickupService("On-Demand pickup", "Giza, 6 October", "1 Box(20Kg)", "400.00"));
        add(new MockedPickupService("Express delivery", "Cairo, Obour", "1 Box(20Kg)", "560.00"));
    }

}
