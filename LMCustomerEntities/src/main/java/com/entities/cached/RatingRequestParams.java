package com.entities.cached;

/**
 * Created by Wafaa on 1/5/2017.
 */

public class RatingRequestParams extends Rating {

    protected double driverRate;
    protected double serviceRate;

    public double getDriverRate() {
        return driverRate;
    }

    public void setDriverRate(double driverRate) {
        this.driverRate = driverRate;
    }

    public double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(double serviceRate) {
        this.serviceRate = serviceRate;
    }
}
