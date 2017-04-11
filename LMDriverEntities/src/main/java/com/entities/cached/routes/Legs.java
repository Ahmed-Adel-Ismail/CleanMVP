package com.entities.cached.routes;

import java.io.PushbackInputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Legs implements Serializable {

    public Legs(){

    }

    private Point to;

    private String arrivalDelay;

    private String pathway;

    private String agencyTimeZoneOffset;

    private List<Steps> steps;

    private String rentedBike;

    private Point from;

    private String endTime;

    private String mode;

    private String startTime;

    private String realTime;

    private String duration;

    private String distance;

    private String interlineWithPreviousLeg;

    private String departureDelay;

    private String route;

    private LegGeometry legGeometry;

    private String transitLeg;

    public Point getTo ()
    {
        return to;
    }

    public void setTo (Point to)
    {
        this.to = to;
    }

    public String getArrivalDelay ()
    {
        return arrivalDelay;
    }

    public void setArrivalDelay (String arrivalDelay)
    {
        this.arrivalDelay = arrivalDelay;
    }

    public String getPathway ()
    {
        return pathway;
    }

    public void setPathway (String pathway)
    {
        this.pathway = pathway;
    }

    public String getAgencyTimeZoneOffset ()
    {
        return agencyTimeZoneOffset;
    }

    public void setAgencyTimeZoneOffset (String agencyTimeZoneOffset)
    {
        this.agencyTimeZoneOffset = agencyTimeZoneOffset;
    }

    public List<Steps> getSteps ()
    {
        return steps;
    }

    public void setSteps (List<Steps> steps)
    {
        this.steps = steps;
    }

    public String getRentedBike ()
    {
        return rentedBike;
    }

    public void setRentedBike (String rentedBike)
    {
        this.rentedBike = rentedBike;
    }

    public Point getFrom ()
    {
        return from;
    }

    public void setFrom (Point from)
    {
        this.from = from;
    }

    public String getEndTime ()
    {
        return endTime;
    }

    public void setEndTime (String endTime)
    {
        this.endTime = endTime;
    }

    public String getMode ()
    {
        return mode;
    }

    public void setMode (String mode)
    {
        this.mode = mode;
    }

    public String getStartTime ()
    {
        return startTime;
    }

    public void setStartTime (String startTime)
    {
        this.startTime = startTime;
    }

    public String getRealTime ()
    {
        return realTime;
    }

    public void setRealTime (String realTime)
    {
        this.realTime = realTime;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getInterlineWithPreviousLeg ()
    {
        return interlineWithPreviousLeg;
    }

    public void setInterlineWithPreviousLeg (String interlineWithPreviousLeg)
    {
        this.interlineWithPreviousLeg = interlineWithPreviousLeg;
    }

    public String getDepartureDelay ()
    {
        return departureDelay;
    }

    public void setDepartureDelay (String departureDelay)
    {
        this.departureDelay = departureDelay;
    }

    public String getRoute ()
    {
        return route;
    }

    public void setRoute (String route)
    {
        this.route = route;
    }

    public LegGeometry getLegGeometry ()
    {
        return legGeometry;
    }

    public void setLegGeometry (LegGeometry legGeometry)
    {
        this.legGeometry = legGeometry;
    }

    public String getTransitLeg ()
    {
        return transitLeg;
    }

    public void setTransitLeg (String transitLeg)
    {
        this.transitLeg = transitLeg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [to = "+to+", arrivalDelay = "+arrivalDelay+", pathway = "+pathway+", agencyTimeZoneOffset = "+agencyTimeZoneOffset+", steps = "+steps+", rentedBike = "+rentedBike+", from = "+from+", endTime = "+endTime+", mode = "+mode+", startTime = "+startTime+", realTime = "+realTime+", duration = "+duration+", distance = "+distance+", interlineWithPreviousLeg = "+interlineWithPreviousLeg+", departureDelay = "+departureDelay+", route = "+route+", legGeometry = "+legGeometry+", transitLeg = "+transitLeg+"]";
    }

}
