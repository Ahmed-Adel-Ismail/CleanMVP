package com.entities.cached.routes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Fare implements Serializable {

    private String details;

    private String fare;

    public String getDetails ()
    {
        return details;
    }

    public void setDetails (String details)
    {
        this.details = details;
    }

    public String getFare ()
    {
        return fare;
    }

    public void setFare (String fare)
    {
        this.fare = fare;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [details = "+details+", fare = "+fare+"]";
    }
}
