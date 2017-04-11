package com.entities.cached.routes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class RequestParameters implements Serializable {

    private String locale;

    private String fromPlace;

    private String toPlace;

    private String mode;

    public String getLocale ()
    {
        return locale;
    }

    public void setLocale (String locale)
    {
        this.locale = locale;
    }

    public String getFromPlace ()
    {
        return fromPlace;
    }

    public void setFromPlace (String fromPlace)
    {
        this.fromPlace = fromPlace;
    }

    public String getToPlace ()
    {
        return toPlace;
    }

    public void setToPlace (String toPlace)
    {
        this.toPlace = toPlace;
    }

    public String getMode ()
    {
        return mode;
    }

    public void setMode (String mode)
    {
        this.mode = mode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [locale = "+locale+", fromPlace = "+fromPlace+", toPlace = "+toPlace+", mode = "+mode+"]";
    }
}
