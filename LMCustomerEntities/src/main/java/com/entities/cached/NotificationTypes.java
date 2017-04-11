package com.entities.cached;

/**
 * Created by Wafaa on 1/6/2017.
 */

public enum NotificationTypes {

    NEW("New")
    , AWAITING_PICKUP("Awaiting Pickup")
    , WAITING_FOR_CUSTOMER_ALTERNATIVE("New")
    , ACTION_NEEDED("New")
    , IN_PICKUP_VERIFICATION("In Pickup Verification")
    , Out_For_Delivery("Out For Delivery")
    , PICKEDUP("Picked Up")
    , CANCELED("Canceled");


    NotificationTypes(final String status) {
        this.status = status;
    }

    public final String status;

    @Override
    public String toString() {
        return status;
    }


}
