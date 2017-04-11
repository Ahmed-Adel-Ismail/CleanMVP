package com.entities.cached.orders;

import java.io.Serializable;
import java.util.Date;

/**
 * the Timing details of the {@link JobOrder}
 * <p>
 * Created by Ahmed Adel on 2/23/2017.
 */
public class JobOrderTiming implements Serializable {

    /**
     * startTimeFromOrigin
     */
    private Date departure;

    /**
     * arrivalTime
     */
    private Date arrival;

    /**
     * timeFrom
     */
    private String pickupStart;

    /**
     * timeTo
     */
    private String pickupEnd;

    /**
     * timeTakenRoutingEngineInText
     */
    private String etaText;

    /**
     * timeTakenValueRoutingEngineInSeconds
     */
    private Long etaSeconds;

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Long getEtaSeconds() {
        return etaSeconds;
    }

    public void setEtaSeconds(Long etaSeconds) {
        this.etaSeconds = etaSeconds;
    }

    public String getEtaText() {
        return etaText;
    }

    public void setEtaText(String etaText) {
        this.etaText = etaText;
    }

    public String getPickupEnd() {
        return pickupEnd;
    }

    public void setPickupEnd(String pickupEnd) {
        this.pickupEnd = pickupEnd;
    }

    public String getPickupStart() {
        return pickupStart;
    }

    public void setPickupStart(String pickupStart) {
        this.pickupStart = pickupStart;
    }
}
