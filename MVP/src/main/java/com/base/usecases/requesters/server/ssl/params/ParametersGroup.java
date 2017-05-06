package com.base.usecases.requesters.server.ssl.params;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.interfaces.Emptyable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Group of Parameter that concatenate to the service URL
 * <p>
 * Created by Wafaa on 10/20/2016.
 */
public class ParametersGroup implements AggregateAddable<Boolean, Parameter>
        , Iterable<Parameter>, Emptyable, Serializable {

    private final List<Parameter> params = new LinkedList<>();


    @Override
    public Iterator<Parameter> iterator() {
        return params.iterator();
    }


    @Override
    public Boolean add(Parameter parameter) {
        return params.add(parameter);
    }

    @Override
    public boolean isEmpty() {
        return params.isEmpty();
    }

    @Override
    public String toString() {
        return params.toString();
    }
}
