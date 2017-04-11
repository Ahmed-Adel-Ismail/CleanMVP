package com.appzoneltd.lastmile.customer.features.main.home;

import java.io.Serializable;

/**
 * Created by Wafaa on 2/21/2017.
 */

public class Addresses implements Serializable{

    private String displayedAddress;
    private String formattedAddress;

    public String getDisplayedAddress() {
        return displayedAddress;
    }

    public void setDisplayedAddress(String displayedAddress) {
        this.displayedAddress = displayedAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
