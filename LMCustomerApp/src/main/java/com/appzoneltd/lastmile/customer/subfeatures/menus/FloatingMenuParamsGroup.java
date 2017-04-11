package com.appzoneltd.lastmile.customer.subfeatures.menus;

import com.base.abstraction.aggregates.AggregateAddable;
import com.base.abstraction.interfaces.Clearable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * a Class that holds a group of {@link FloatingMenuParams} instances
 * <p/>
 * Created by Ahmed Adel on 9/5/2016.
 */
public class FloatingMenuParamsGroup implements
        Serializable,
        Clearable,
        Iterable<FloatingMenuParams>,
        AggregateAddable<Boolean, FloatingMenuParams> {


    private final LinkedList<FloatingMenuParams> floatingMenuParamsList = new LinkedList<>();

    @Override
    public Boolean add(FloatingMenuParams floatingMenuParams) {
        return floatingMenuParamsList.add(floatingMenuParams);
    }

    @Override
    public void clear() {
        floatingMenuParamsList.clear();
    }

    @Override
    public Iterator<FloatingMenuParams> iterator() {
        return floatingMenuParamsList.iterator();
    }
}
