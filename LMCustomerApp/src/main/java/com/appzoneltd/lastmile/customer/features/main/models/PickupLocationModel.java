package com.appzoneltd.lastmile.customer.features.main.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Wafaa on 9/28/2016.
 */
public class PickupLocationModel implements Serializable {

    private boolean isValidAddress;
    private String formattedAddress;
    private String displayedAddress;
    private String pickupLongitude;
    private String pickupLatitude;


    public void setValidAddress(boolean validAddress) {
        isValidAddress = validAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public void setGoogleAddress(LatLng latLng) {
        this.pickupLatitude = String.valueOf(latLng.latitude);
        this.pickupLongitude = String.valueOf(latLng.longitude);
    }

    public boolean isValidAddress() {
        return isValidAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public String getDisplayedAddress() {
        return displayedAddress;
    }

    public void setDisplayedAddress(String displayedAddress) {
        this.displayedAddress = displayedAddress;
    }
}
