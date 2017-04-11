package com.entities.requesters;

import com.entities.cached.Package;
import com.entities.cached.Pickup;

import java.io.Serializable;

/**
 * Created by Wafaa on 6/2/2016.
 */
public class CreatePickupRequestParams implements Serializable {

    private Pickup pickupRequest;
    private Package packge;


    public CreatePickupRequestParams() {

    }

    public Package getPackge() {
        return packge;
    }

    public void setPackge(Package packge) {
        this.packge = packge;
    }

    public Pickup getPickupRequest() {
        return pickupRequest;
    }

    public void setPickupRequest(Pickup pickupRequest) {
        this.pickupRequest = pickupRequest;
    }
}
