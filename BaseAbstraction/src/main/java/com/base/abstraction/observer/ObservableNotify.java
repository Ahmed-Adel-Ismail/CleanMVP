package com.base.abstraction.observer;

import com.base.abstraction.events.Event;

/**
 * an interface that holds the notifying methods for an Observable pattern
 * <p>
 * Created by Ahmed Adel on 9/19/2016.
 */
public interface ObservableNotify {

    /**
     * notify all {@link Observer} instances for event
     *
     * @param event the {@link Event} that holds the event
     * @throws RuntimeException if an error occurred
     */
    void notifyObservers(Event event) throws RuntimeException;
}
