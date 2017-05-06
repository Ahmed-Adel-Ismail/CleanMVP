package com.base.presentation.exceptions.locations;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * a {@link RuntimeException} that indicates that connection has suspended from location services
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class ConnectionSuspendedException extends RuntimeException {

    private static final String CAUSE_NETWORK_LOST = "network lost";
    private static final String CAUSE_SERVICE_DISCONNECTED = "google play services disconnected";
    private static final String CAUSE_UNKNOWN = "unknown";

    public ConnectionSuspendedException(int cause) {
        super(retrieveCauseString(cause));

    }

    private static String retrieveCauseString(int cause) {
        String causeString;
        switch (cause) {
            case GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST:
                causeString = CAUSE_NETWORK_LOST;
                break;
            case GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED:
                causeString = CAUSE_SERVICE_DISCONNECTED;
                break;
            default:
                causeString = CAUSE_UNKNOWN;
        }
        return causeString;
    }
}
