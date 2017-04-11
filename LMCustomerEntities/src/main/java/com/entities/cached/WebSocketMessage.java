package com.entities.cached;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 12/21/2016.
 */

public class WebSocketMessage implements Serializable{

    private String driverName;
    private String driverNumber;
    private long hubId;
    private TrackingLocation location;
    private long vehicleId;
    private String vehicleStatus;
    private double rating;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public long getHubId() {
        return hubId;
    }

    public void setHubId(long hubId) {
        this.hubId = hubId;
    }

    public TrackingLocation getLocation() {
        return location;
    }

    public void setLocation(TrackingLocation location) {
        this.location = location;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
