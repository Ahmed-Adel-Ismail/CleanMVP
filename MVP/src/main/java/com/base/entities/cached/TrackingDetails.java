package com.base.entities.cached;

import java.io.Serializable;

/**
 * a class that holds the data required for tracking the device
 * <p>
 * Created by Ahmed Adel on 2/21/2017.
 */
public class TrackingDetails implements Serializable {

    private Location location;
    private PushNotificationTokens pushNotificationTokens;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PushNotificationTokens getPushNotificationTokens() {
        return pushNotificationTokens;
    }

    public void setPushNotificationTokens(PushNotificationTokens pushNotificationTokens) {
        this.pushNotificationTokens = pushNotificationTokens;
    }
}
