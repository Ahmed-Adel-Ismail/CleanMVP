package com.entities.cached.routes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Itineraries implements Serializable {

    public Itineraries(){

    }

    protected String transfers;

    protected String walkTime;

    protected String tooSloped;

    protected Fare fare;

    protected String walkDistance;

    protected String endTime;

    protected String elevationGained;

    protected String startTime;

    protected String duration;

    protected String waitingTime;

    protected String elevationLost;

    protected String walkLimitExceeded;

    protected List<Legs> legs;

    protected String transitTime;

    public String getTransfers ()
    {
        return transfers;
    }

    public void setTransfers (String transfers)
    {
        this.transfers = transfers;
    }

    public String getWalkTime ()
    {
        return walkTime;
    }

    public void setWalkTime (String walkTime)
    {
        this.walkTime = walkTime;
    }

    public String getTooSloped ()
    {
        return tooSloped;
    }

    public void setTooSloped (String tooSloped)
    {
        this.tooSloped = tooSloped;
    }

    public Fare getFare ()
    {
        return fare;
    }

    public void setFare (Fare fare)
    {
        this.fare = fare;
    }

    public String getWalkDistance ()
    {
        return walkDistance;
    }

    public void setWalkDistance (String walkDistance)
    {
        this.walkDistance = walkDistance;
    }

    public String getEndTime ()
    {
        return endTime;
    }

    public void setEndTime (String endTime)
    {
        this.endTime = endTime;
    }

    public String getElevationGained ()
    {
        return elevationGained;
    }

    public void setElevationGained (String elevationGained)
    {
        this.elevationGained = elevationGained;
    }

    public String getStartTime ()
    {
        return startTime;
    }

    public void setStartTime (String startTime)
    {
        this.startTime = startTime;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getWaitingTime ()
    {
        return waitingTime;
    }

    public void setWaitingTime (String waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    public String getElevationLost ()
    {
        return elevationLost;
    }

    public void setElevationLost (String elevationLost)
    {
        this.elevationLost = elevationLost;
    }

    public String getWalkLimitExceeded ()
    {
        return walkLimitExceeded;
    }

    public void setWalkLimitExceeded (String walkLimitExceeded)
    {
        this.walkLimitExceeded = walkLimitExceeded;
    }

    public List<Legs> getLegs ()
    {
        return legs;
    }

    public void setLegs (List<Legs> legs)
    {
        this.legs = legs;
    }

    public String getTransitTime ()
    {
        return transitTime;
    }

    public void setTransitTime (String transitTime)
    {
        this.transitTime = transitTime;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [transfers = "+transfers+", walkTime = "+walkTime+", tooSloped = "+tooSloped+", fare = "+fare+", walkDistance = "+walkDistance+", endTime = "+endTime+", elevationGained = "+elevationGained+", startTime = "+startTime+", duration = "+duration+", waitingTime = "+waitingTime+", elevationLost = "+elevationLost+", walkLimitExceeded = "+walkLimitExceeded+", legs = "+legs+", transitTime = "+transitTime+"]";
    }

}
