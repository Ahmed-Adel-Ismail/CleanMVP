package com.base.abstraction.observer;

/**
 * A Class that can be used through an {@link EventsManager}, where it can
 * Observe to it's {@link com.base.abstraction.events.Event Events}
 * and notify it with {@link com.base.abstraction.events.Event Events} as well
 * <p/>
 * <u> usually implementers of this interface tend to communicate with other classes through
 * {@link com.base.abstraction.events.Event Events}, which means that they should not be
 * assigned to member variables, they should be assigned through
 * {@link EventsManager#addEventsSubscriber(EventsSubscriber)}</u>
 * <p/>
 * Created by Ahmed Adel on 9/15/2016.
 */
public interface EventsSubscriber extends Observable, Observer {

}
