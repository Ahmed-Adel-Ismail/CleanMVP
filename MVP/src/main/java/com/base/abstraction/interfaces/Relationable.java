package com.base.abstraction.interfaces;

/**
 * an interface implemented by Classes that will be used in Relation based implementation,
 * like Databases for example
 * <p/>
 * Created by Ahmed Adel on 8/30/2016.
 */
public interface Relationable {

    /**
     * get the ID of the current instance
     *
     * @return a ID value
     */
    long getId();

}
