package com.appzoneltd.lastmile.customer.system;

/**
 * a class responsible for tracking the loaded data state for the application for the current
 * session
 * <p>
 * Created by Ahmed Adel on 11/17/2016.
 */
public class SystemLoader {

    private boolean firebaseTokenRefreshed;

    public boolean isFirebaseTokenRefreshed() {
        return firebaseTokenRefreshed;
    }

    public void setFirebaseTokenRefreshed(boolean pushNotificationsTokenRefreshed) {
        this.firebaseTokenRefreshed = pushNotificationsTokenRefreshed;
    }

    public boolean isReady() {
        return firebaseTokenRefreshed;
    }

}
