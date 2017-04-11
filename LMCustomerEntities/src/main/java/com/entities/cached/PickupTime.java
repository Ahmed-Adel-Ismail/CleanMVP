package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Ahmed Adel on 9/21/2016.
 */
public class PickupTime implements Serializable {

    private Long pickupTimeId;
    private String fromTime;
    private String toTime;

    public long getPickupTimeId() {
        return pickupTimeId;
    }

    public void setPickupTimeId(long pickupTimeId) {
        this.pickupTimeId = pickupTimeId;
    }

    public void setPickupTimeId(Long pickupTimeId) {
        this.pickupTimeId = pickupTimeId;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }


    public String createPickupTimeItem(){
        return this.fromTime + " - " + this.toTime;
    }

}
