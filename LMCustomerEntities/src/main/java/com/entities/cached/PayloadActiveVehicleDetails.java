package com.entities.cached;

import java.io.Serializable;

/**
 * Created by Wafaa on 12/26/2016.
 */

public class PayloadActiveVehicleDetails implements Serializable {

    protected String driverName;
    protected String driverPhoneNumber;
    protected long driverRating;
    protected long driverImageId;
    protected String vehicleModel;
    protected String vehiclePlateNumber;
    protected long vehicleId;
    protected long hubId;
    protected long driverId;
    protected long requestId;
    protected double pickupLatitude;
    protected double pickupLongitude;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public long getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(long driverRating) {
        this.driverRating = driverRating;
    }

    public long getDriverImageId() {
        return driverImageId;
    }

    public void setDriverImageId(long driverImageId) {
        this.driverImageId = driverImageId;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getHubId() {
        return hubId;
    }

    public void setHubId(long hubId) {
        this.hubId = hubId;
    }

    public double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public void setPickupLatitude(long pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }


    public void setPickupLongitude(long pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
