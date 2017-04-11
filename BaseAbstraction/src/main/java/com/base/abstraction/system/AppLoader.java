package com.base.abstraction.system;

/**
 * a class that checks weather the data are loaded for this application so it can operate normally,
 * or it will need to loadAnnotatedElements it's data first ... the default implementation for this class checks
 * only for very basic data like push-notifications
 * <p>
 * Created by Ahmed Adel on 11/20/2016.
 */
public class AppLoader {

    private boolean firebaseTokenRefreshed;

    public boolean isFirebaseTokenRefreshed() {
        return firebaseTokenRefreshed;
    }

    public void setFirebaseTokenRefreshed(boolean pushNotificationsTokenRefreshed) {
        this.firebaseTokenRefreshed = pushNotificationsTokenRefreshed;
    }

    /**
     * check if the application is ready to display it's normal screens or not
     *
     * @return {@code true} if the flow should go normally, else the application shuold display the
     * loading / splash screen
     */
    public boolean isReady() {
        return isFirebaseTokenRefreshed();
    }
}
