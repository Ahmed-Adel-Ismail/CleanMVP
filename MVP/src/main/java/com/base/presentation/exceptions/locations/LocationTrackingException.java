package com.base.presentation.exceptions.locations;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.observer.Observer;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.locations.LocationTracking;

/**
 * the base class for {@link LocationTracking} related Exceptions
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public abstract class LocationTrackingException extends RuntimeException
        implements Command<AbstractActivity, Void>, Observer {

    private final Executor<Message> executor;

    LocationTrackingException() {
        this.executor = new Executor<>();
        this.executor.putAll(createExecutor());
    }

    /**
     * create the {@link Executor} to listen to the {@link AbstractActivity} events
     * when this class acts as an {@link Observer}
     *
     * @return the {@link Executor}
     */
    protected Executor<Message> createExecutor() {
        return null;
    }

    /**
     * invoke this method to handle the thrown {@link LocationTrackingException} sub-class,
     * each sub-class knows how to fix it-self
     *
     * @param abstractActivity the {@link AbstractActivity} that will host the operation
     * @return nothing
     */
    @Override
    public abstract Void execute(AbstractActivity abstractActivity);


    /**
     * attach self as Observer on the host {@link AbstractActivity}
     *
     * @param activity the host {@link AbstractActivity}
     */
    protected final void addObserver(AbstractActivity activity) {
        if (activity != null) {
            activity.addObserver(this);
        }
    }

    /**
     * remove self from being Observer on the host {@link AbstractActivity}
     *
     * @param activity the host {@link AbstractActivity}
     */
    protected final void removeObserver(AbstractActivity activity) {
        if (activity != null) {
            activity.removeObserver(this);
        }
    }


    @Override
    public final void onUpdate(Event event) throws RuntimeException {
        executor.execute(event.getId(), event.getMessage());
    }
}
