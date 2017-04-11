package com.base.presentation.base.abstracts.features;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.EventLogger;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.presentation.R;

/**
 * a {@link Command} that is executed by the Fragment's
 * {@link com.base.abstraction.messaging.Mailbox#execute(Object)}
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
class FragmentMailboxExecutor implements Command<Event, Void> {

    private EventLogger selfUpdateLogger;
    private EventLogger onUpdateLogger;
    private CheckedReference<AbstractFragment> fragmentRef;

    FragmentMailboxExecutor(@NonNull AbstractFragment fragment) {
        this.fragmentRef = new CheckedReference<>(fragment);
        this.selfUpdateLogger = new EventLogger(fragment.getClass(), "ignored self onUpdate");
        this.onUpdateLogger = new EventLogger(fragment.getClass(), "onUpdate");
    }

    @Override
    public Void execute(Event event) {
        try {
            AbstractFragment fragment = fragmentRef.get();
            if (isValidOnUpdate(fragment, event)) {
                doOnUpdate(fragment, event);
            }
        } catch (CheckedReferenceClearedException e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }

    private boolean isValidOnUpdate(AbstractFragment fragment, Event event) {
        boolean invalid = (fragment.getActorAddress() == event.getSenderActorAddress())
                || isActivityLifeCycleEvent(event.getId());
        if (invalid) {
            selfUpdateLogger.execute(event);
        }
        return !invalid;
    }

    private boolean isActivityLifeCycleEvent(long id) {
        return id == R.id.onCreate
                || id == R.id.onResume
                || id == R.id.onPause
                || id == R.id.onDestroy
                || id == R.id.onStart
                || id == R.id.onStop;
    }

    private void doOnUpdate(AbstractFragment fragment, Event event) {
        onUpdateLogger.execute(event);
        invokeOnUpdateAndNotifyActivity(fragment, event);
    }


    private void invokeOnUpdateAndNotifyActivity(AbstractFragment fragment, Event event) {
        fragment.getViewBinder().onUpdate(event);
        if (event.getSenderActorAddress() != fragment.getHostActivity().getActorAddress()) {
            notifyActivity(fragment, event);
        }

    }

    private void notifyActivity(AbstractFragment fragment, Event event) {
        Event.Builder builder = new Event.Builder(event.getId());
        builder.senderActorAddress(fragment.getActorAddress());
        builder.message(event.getMessage());
        Event newEvent = builder.build();
        fragment.getHostActivity().onUpdate(newEvent);
    }
}
