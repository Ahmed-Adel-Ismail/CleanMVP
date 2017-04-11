package com.entities.cached.routes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Steps implements Serializable {

    public Steps() {

    }

    public Steps(double lng, double lat) {
        this.lat = lat;
        this.lon = lng;
    }

    protected String absoluteDirection;

    protected double lon;

    protected String distance;

    protected String area;

    protected String stayOn;

    protected String relativeDirection;

    protected String[] elevation;

    protected String streetName;

    protected String bogusName;

    protected double lat;

    public String getAbsoluteDirection() {
        return absoluteDirection;
    }

    public void setAbsoluteDirection(String absoluteDirection) {
        this.absoluteDirection = absoluteDirection;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStayOn() {
        return stayOn;
    }

    public void setStayOn(String stayOn) {
        this.stayOn = stayOn;
    }

    public String getRelativeDirection() {
        return relativeDirection;
    }

    public void setRelativeDirection(String relativeDirection) {
        this.relativeDirection = relativeDirection;
    }

    public String[] getElevation() {
        return elevation;
    }

    public void setElevation(String[] elevation) {
        this.elevation = elevation;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBogusName() {
        return bogusName;
    }

    public void setBogusName(String bogusName) {
        this.bogusName = bogusName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "ClassPojo [absoluteDirection = " + absoluteDirection + ", lon = " + lon + ", distance = " + distance + ", area = " + area + ", stayOn = " + stayOn + ", relativeDirection = " + relativeDirection + ", elevation = " + elevation + ", streetName = " + streetName + ", bogusName = " + bogusName + ", lat = " + lat + "]";
    }
}
