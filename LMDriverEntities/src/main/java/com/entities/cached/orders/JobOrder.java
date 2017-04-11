package com.entities.cached.orders;

import com.base.cached.Location;

import java.io.Serializable;

/**
 * the Job Order for the Driver
 * <p>
 * Created by Ahmed Adel on 2/23/2017.
 */
public class JobOrder implements Serializable, Comparable<JobOrder> {

    /**
     * id
     */
    private long id;

    /**
     * orderPackageId
     */
    private long packageId;

    /**
     * priority
     */
    private int priority;

    /**
     * actualWeight
     */
    private double weight;

    /**
     * jobAddress
     */
    private String address;

    /**
     * jobLatitude
     * jobLongitude
     */
    private Location location;


    /**
     * all the time parameters moved to this object
     */
    private JobOrderTiming timing;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public JobOrderTiming getTiming() {
        return timing;
    }

    public void setTiming(JobOrderTiming timing) {
        this.timing = timing;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobOrder)) return false;

        JobOrder jobOrder = (JobOrder) o;

        return id == jobOrder.id
                && packageId == jobOrder.packageId
                && priority == jobOrder.priority;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (packageId ^ (packageId >>> 32));
        result = 31 * result + priority;
        return result;
    }

    @Override
    public int compareTo(JobOrder otherJobOrder) {
        return Integer.valueOf(priority).compareTo(otherJobOrder.priority);
    }
}

















