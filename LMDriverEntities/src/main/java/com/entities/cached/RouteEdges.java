package com.entities.cached;

import com.base.annotations.MockEntity;
import com.entities.mocks.MockedRouteEdges;

import java.io.Serializable;

/**
 * Created by Wafaa on 2/6/2017.
 */

@MockEntity(MockedRouteEdges.class)
public class RouteEdges implements Serializable {

    public RouteEdges(){

    }

    protected double sourceLat;
    protected double sourceLng;
    protected double destinationLat;
    protected double destinationLng;

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLng() {
        return sourceLng;
    }

    public void setSourceLng(double sourceLng) {
        this.sourceLng = sourceLng;
    }
}
