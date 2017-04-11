package com.base.presentation.base.abstracts.features;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.EventLogger;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.R;

/**
 * the {@link Command} that is executed in the
 * {@link com.base.abstraction.messaging.Mailbox#execute(Object)} for the
 * {@link AbstractActivity}
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
class ActivityMailboxExecutor implements Command<Event, Void> {

    private EventLogger selfUpdateLogger;
    private EventLogger onUpdateLogger;
    private CheckedReference<AbstractActivity> activityRef;

    ActivityMailboxExecutor(@NonNull AbstractActivity activity) {
        this.activityRef = new CheckedReference<>(activity);
        this.selfUpdateLogger = new EventLogger(activity.getClass(), "ignored onUpdate");
        this.onUpdateLogger = new EventLogger(activity.getClass(), "onUpdate");
    }

    @Override
    public Void execute(Event event) {
        try {
            doExecute(event);
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }

    private void doExecute(Event event) {
        AbstractActivity activity = activityRef.get();
        if (activity.getActorAddress() == event.getSenderActorAddress()) {
            selfUpdateLogger.execute(event);
        } else if (event.getId() == R.id.onRepositoryResponse) {
            doOnUpdate(activity.getViewBinder(), createOnRepositoryResponseEvent(event, activity));
        } else {
            doOnUpdate(activity.getViewBinder(), event);
        }
    }

    private Event createOnRepositoryResponseEvent(Event event, AbstractActivity activity) {
        return new EventBuilder(event.getId(), event.getMessage(),
                event.getReceiverActorAddresses()).execute(activity);
    }

    private void doOnUpdate(ViewBinder viewBinder, Event event) {
        onUpdateLogger.execute(event);
        viewBinder.onUpdate(event);
    }

}
