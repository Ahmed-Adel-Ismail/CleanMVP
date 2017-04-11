package com.entities.mocks;

import com.entities.cached.NotificationTypes;
import com.entities.cached.PickupStatus;
import com.entities.cached.PickupStatusGroup;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class MockedPickupStatusGroup  extends PickupStatusGroup {

    public MockedPickupStatusGroup(){
        add(new PickupStatus("gift", "Misbah", "October", NotificationTypes.NEW));
        add(new PickupStatus("", "Mahmoud", "October", NotificationTypes.ACTION_NEEDED));
        add(new PickupStatus("", "Adel", "Mohandsen", NotificationTypes.AWAITING_PICKUP));
        add(new PickupStatus("watch", "Mariam", "Alex", NotificationTypes.CANCELED));
    }

}
