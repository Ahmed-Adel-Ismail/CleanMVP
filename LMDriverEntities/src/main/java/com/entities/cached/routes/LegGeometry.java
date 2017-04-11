package com.entities.cached.routes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class LegGeometry implements Serializable {

    private String length;

    private String points;

    public String getLength ()
    {
        return length;
    }

    public void setLength (String length)
    {
        this.length = length;
    }

    public String getPoints ()
    {
        return points;
    }

    public void setPoints (String points)
    {
        this.points = points;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [length = "+length+", points = "+points+"]";
    }

}
