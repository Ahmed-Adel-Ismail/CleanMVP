package com.base.usecases.callbacks;

import com.base.abstraction.events.Event;
import com.base.abstraction.observer.Observer;

/**
 * an interface similar to {@link Observer}
 * but implemented by Classes waiting for Call-backs (like server callbacks)
 * <p/>
 * This interface is created to avoid overlapping of Events, specially while dealing
 * with Server requests, as the Callback listener Class calls {@link Observer#onUpdate} on the
 * Server related class, and when it finishes it invokes {@link Observer#onUpdate} for the
 * Call-back listener class ... overlapping happens since both observe on each other,
 * so both will keep handling the same {@link Event}
 * <p/>
 * but implementing this interface makes the Callback class a normal listener that is passed
 * through the other class' constructor and be used in a different (but similar) manner
 * <p/>
 * Created by Ahmed Adel on 9/19/2016.
 *
 * @see CallbackHolder
 * @see CallbackDispatcher
 */
public interface Callback {

    /**
     * invoked when a call-back is required to be invoked, like for example when a request to a
     * server finishes and want to notify it's Call-back listener class
     *
     * @param event the {@link Event} that holds the update
     */
    void onCallback(Event event);
}
