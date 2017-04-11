package com.entities.cached.routes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wafaa on 1/30/2017.
 */

public class DebugOutput implements Serializable {

    public DebugOutput(){

    }

    private List<String> pathTimes;

    private String renderingTime;

    private String timedOut;

    private String pathCalculationTime;

    private String totalTime;

    private String precalculationTime;

    public List<String> getPathTimes ()
    {
        return pathTimes;
    }

    public void setPathTimes (List<String> pathTimes)
    {
        this.pathTimes = pathTimes;
    }

    public String getRenderingTime ()
    {
        return renderingTime;
    }

    public void setRenderingTime (String renderingTime)
    {
        this.renderingTime = renderingTime;
    }

    public String getTimedOut ()
    {
        return timedOut;
    }

    public void setTimedOut (String timedOut)
    {
        this.timedOut = timedOut;
    }

    public String getPathCalculationTime ()
    {
        return pathCalculationTime;
    }

    public void setPathCalculationTime (String pathCalculationTime)
    {
        this.pathCalculationTime = pathCalculationTime;
    }

    public String getTotalTime ()
    {
        return totalTime;
    }

    public void setTotalTime (String totalTime)
    {
        this.totalTime = totalTime;
    }

    public String getPrecalculationTime ()
    {
        return precalculationTime;
    }

    public void setPrecalculationTime (String precalculationTime)
    {
        this.precalculationTime = precalculationTime;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pathTimes = "+pathTimes+", renderingTime = "+renderingTime+", timedOut = "+timedOut+", pathCalculationTime = "+pathCalculationTime+", totalTime = "+totalTime+", precalculationTime = "+precalculationTime+"]";
    }

}
