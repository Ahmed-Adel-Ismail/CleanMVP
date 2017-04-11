package com.entities.mocks;

import com.entities.cached.NotificationTypes;
import com.entities.cached.PickupStatus;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class MockedPickupStatus extends PickupStatus {

    public MockedPickupStatus(String nName, String rName, String rAddress, NotificationTypes status) {
        super.nickname = nName;
        super.recipientName = rName;
        super.recipientAddress = rAddress;
        super.status = status;
    }

}
