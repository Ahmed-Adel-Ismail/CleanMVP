package com.base.abstraction.observer;

import com.base.abstraction.events.Event;

/**
 * implement this interface if your class will receive Updates from another class, like the
 * Observer pattern Observers
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public interface Updatable {

    /**
     * notify the observer that an event occurred
     *
     * @param event the {@link Event}
     * @throws RuntimeException a sub-class of {@link RuntimeException} that will indicate an
     *                          error
     */
    void onUpdate(Event event) throws RuntimeException;
}
