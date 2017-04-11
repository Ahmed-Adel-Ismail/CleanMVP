package com.entities.cached.routes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Point implements Serializable {

    public Point(){

    }

    private String vertexType;

    protected double lon;

    private String name;

    private String orig;

    protected double lat;

    public String getVertexType ()
    {
        return vertexType;
    }

    public void setVertexType (String vertexType)
    {
        this.vertexType = vertexType;
    }

    public double getLon ()
    {
        return lon;
    }

    public void setLon (double lon)
    {
        this.lon = lon;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getOrig ()
    {
        return orig;
    }

    public void setOrig (String orig)
    {
        this.orig = orig;
    }

    public double getLat ()
    {
        return lat;
    }

    public void setLat (double lat)
    {
        this.lat = lat;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vertexType = "+vertexType+", lon = "+lon+", name = "+name+", orig = "+orig+", lat = "+lat+"]";
    }
}
