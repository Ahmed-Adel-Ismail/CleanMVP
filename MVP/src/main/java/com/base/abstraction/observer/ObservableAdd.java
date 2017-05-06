package com.base.abstraction.observer;

import com.base.abstraction.events.Event;

/**
 * an interface that holds the Adding methods for an Observable pattern
 * <p>
 * Created by Ahmed Adel on 9/19/2016.
 */
public interface ObservableAdd {

    /**
     * add an observer to your class, which will be notified later when the
     * {@link Observable#notifyObservers(Event)} is invoked
     *
     * @param observer a {@link Observer} to be notified
     */
    void addObserver(Observer observer);
}
