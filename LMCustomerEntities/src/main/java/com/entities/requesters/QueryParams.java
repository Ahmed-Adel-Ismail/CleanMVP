package com.entities.requesters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 12/20/2016.
 */

public class QueryParams implements Serializable {

    protected long hubId;
    protected long vehicleId;
    protected long driverId;


    public long getHubId() {
        return hubId;
    }

    public void setHubId(long hubId) {
        this.hubId = hubId;
    }


    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
