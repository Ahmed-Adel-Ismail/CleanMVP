package com.base.usecases.requesters.server.ssl.params;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.interfaces.Emptyable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Group of path variables added to the url
 * <p>
 * Created by Wafaa on 10/30/2016.
 */
public class PathVariableGroup implements AggregateAddable<Boolean, Serializable>
        , Iterable<Serializable>, Emptyable, Serializable {

    private List<Serializable> variables = new ArrayList<>();

    @Override
    public Boolean add(Serializable pathVariable) {
        return variables.add(pathVariable);
    }

    @Override
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    @Override
    public Iterator<Serializable> iterator() {
        return variables.iterator();
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
