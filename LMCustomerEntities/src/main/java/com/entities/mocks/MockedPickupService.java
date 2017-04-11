package com.entities.mocks;

import com.entities.cached.PickupService;

import java.math.BigDecimal;

/**
 * Created by Wafaa on 12/28/2016.
 */

class MockedPickupService extends PickupService {

    MockedPickupService(String type, String address
            , String weight, String price) {

        super.type = type;
        super.location = address;
        super.quantity = weight;
        super.price = price;
    }
}
