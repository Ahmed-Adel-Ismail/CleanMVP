package com.base.presentation.base.commands;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.Future;
import com.base.abstraction.events.Event;
import com.base.presentation.R;

/**
 * a class that detects the life cycle events and returns it in the {@link Future} instance passed
 * back, if the {@link Event} passed is not a life cycle event, the {@link Future} instance will
 * invoke it's {@link Future#onComplete(Command)} with a {@code null} parameter
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class LifeCycleValidator implements Command<Event, Future<Event>> {

    @Override
    public Future<Event> execute(Event event) {
        Future<Event> future = new Future<>();
        if (isLifeCycleEvent(event.getId())) {
            future.setResult(event);
        } else {
            future.setResult(null);
        }
        return future;
    }

    private boolean isLifeCycleEvent(long eventId) {
        return eventId == R.id.onCreate
                || eventId == R.id.onStart
                || eventId == R.id.onResume
                || eventId == R.id.onPause
                || eventId == R.id.onStop
                || eventId == R.id.onDestroy
                || eventId == R.id.onSaveInstanceState
                || eventId == R.id.onLowMemory;
    }
}