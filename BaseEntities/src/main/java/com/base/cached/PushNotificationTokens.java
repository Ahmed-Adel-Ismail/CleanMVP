package com.base.cached;

import java.io.Serializable;

/**
 * a class that holds the push-notification tokens values
 * <p>
 * Created by Ahmed Adel on 12/13/2016.
 */
public class PushNotificationTokens implements Serializable {

    private String firebaseToken;

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
