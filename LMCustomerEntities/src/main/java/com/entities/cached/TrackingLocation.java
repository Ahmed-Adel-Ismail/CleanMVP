package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/26/2016.
 */

public class TrackingLocation implements Serializable {

    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
