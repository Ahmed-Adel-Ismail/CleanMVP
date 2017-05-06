package com.base.usecases.events;

import com.base.usecases.requesters.server.ssl.params.ParametersGroup;
import com.base.usecases.requesters.server.ssl.params.PathVariableGroup;

import java.io.Serializable;

/**
 * Created by Ahmed Adel on 11/7/2016.
 */

public class RequestEventBuilderParams {

    long requestId;
    public ParametersGroup parametersGroup;
    public PathVariableGroup pathVariableGroup;
    public Serializable entity;

    public RequestEventBuilderParams(long requestId) {
        this.requestId = requestId;
    }
}
