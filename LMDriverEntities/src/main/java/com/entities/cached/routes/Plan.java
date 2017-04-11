package com.entities.cached.routes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class Plan implements Serializable {

    private Point to;

    private List<Itineraries> itineraries;

    private Point from;

    private String date;

    public Point getTo ()
    {
        return to;
    }

    public void setTo (Point to)
    {
        this.to = to;
    }

    public List<Itineraries> getItineraries ()
    {
        return itineraries;
    }

    public void setItineraries (List<Itineraries> itineraries)
    {
        this.itineraries = itineraries;
    }

    public Point getFrom ()
    {
        return from;
    }

    public void setFrom (Point from)
    {
        this.from = from;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [to = "+to+", itineraries = "+itineraries+", from = "+from+", date = "+date+"]";
    }

}
