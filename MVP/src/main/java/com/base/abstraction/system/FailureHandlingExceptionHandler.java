package com.base.abstraction.system;

import android.os.Looper;

import com.base.abstraction.R;
import com.base.abstraction.messaging.AbstractMailbox;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.EventBuilder;
import com.base.abstraction.exceptions.failures.Failure;

class FailureHandlingExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    FailureHandlingExceptionHandler() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof Failure && isBackgroundThread(thread)) {
            handleFailure(thread, (Failure) throwable);
        } else {
            defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }

    private void handleFailure(Thread thread, Failure failure) {
        try {
            AbstractMailbox<Event> mailbox = App.getInstance().getActorSystem()
                    .getByBackgroundThread(thread);
            failure.execute(new EventBuilder(R.id.onFailureException, mailbox).execute(null));
        } catch (Throwable e) {
            defaultUncaughtExceptionHandler.uncaughtException(thread, e);
        }

    }

    private boolean isBackgroundThread(Thread thread) {
        return thread.getId() != Looper.getMainLooper().getThread().getId();
    }
}
