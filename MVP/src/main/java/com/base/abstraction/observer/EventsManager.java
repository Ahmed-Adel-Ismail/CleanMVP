package com.base.abstraction.observer;

/**
 * a class that can hold multiple {@link EventsSubscriber} classes, where it can notify them and
 * also get notified by them through {@link com.base.abstraction.events.Event Events}
 * <p/>
 * Created by Ahmed Adel on 9/15/2016.
 */
public interface EventsManager extends EventsSubscriber {

    /**
     * start an Events subscription between both this {@link EventsManager} and the passed
     * {@link EventsSubscriber} class, where both classes are now listening to each others Events
     *
     * @param eventsSubscriber the other {@link EventsSubscriber} that will initiate a
     *                         subscription with
     */
    void addEventsSubscriber(EventsSubscriber eventsSubscriber);

    /**
     * stop the connection between both {@link EventsSubscriber} classes, where both
     * {@link EventsSubscriber}
     * classes will stop Observing on each other and will not notify each other any more
     *
     * @param eventsSubscriber the other {@link EventsSubscriber} that is already subscribed to
     *                         this {@link EventsManager}
     */
    void removeEventsSubscriber(EventsSubscriber eventsSubscriber);

}
