package com.entities.cached.routes;

import com.base.annotations.MockEntity;
import com.entities.mocks.routes.MockedRoutes;

import java.io.Serializable;

/**
 * Created by Wafaa on 1/30/2017.
 */

@MockEntity(MockedRoutes.class)
public class Routes implements Serializable{

    public Routes(){

    }

    protected DebugOutput debugOutput;

    protected Plan plan;

    protected RequestParameters requestParameters;

    public DebugOutput getDebugOutput ()
    {
        return debugOutput;
    }

    public void setDebugOutput (DebugOutput debugOutput)
    {
        this.debugOutput = debugOutput;
    }

    public Plan getPlan ()
    {
        return plan;
    }

    public void setPlan (Plan plan)
    {
        this.plan = plan;
    }

    public RequestParameters getRequestParameters ()
    {
        return requestParameters;
    }

    public void setRequestParameters (RequestParameters requestParameters)
    {
        this.requestParameters = requestParameters;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [debugOutput = "+debugOutput+", plan = "+plan+", requestParameters = "+requestParameters+"]";
    }

}
