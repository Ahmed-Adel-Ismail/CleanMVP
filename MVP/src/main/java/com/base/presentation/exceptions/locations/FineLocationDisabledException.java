package com.base.presentation.exceptions.locations;

import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.Executor;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.references.CheckedReference;
import com.base.abstraction.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.system.FineLocationDialog;
import com.base.presentation.system.FineLocationDialogAction;

/**
 * a {@link RuntimeException} thrown when the GPS location is disabled
 * <p>
 * Created by Ahmed Adel on 12/20/2016.
 */
public class FineLocationDisabledException extends LocationTrackingException {

    private final CheckedReference<AbstractActivity> activityReference;
    private FineLocationDialog dialog;

    public FineLocationDisabledException() {
        activityReference = new CheckedReference<>(null);
    }

    @Override
    public Void execute(AbstractActivity abstractActivity) {
        activityReference.set(abstractActivity);
        addObserver(abstractActivity);
        try {
            dialog = new FineLocationDialog();
            dialog.execute(abstractActivity).onComplete(onComplete());
        } catch (Throwable e) {
            Logger.getInstance().exception(e);
        }
        return null;
    }

    @NonNull
    private Command<FineLocationDialogAction, Void> onComplete() {
        return new Command<FineLocationDialogAction, Void>() {
            @Override
            public Void execute(FineLocationDialogAction action) {
                AbstractActivity activity = activityReference.get();
                action.process(activity).onComplete(onCompleteProcess());
                return null;
            }
        };
    }

    @NonNull
    private Command<Boolean, Void> onCompleteProcess() {
        return new Command<Boolean, Void>() {
            @Override
            public Void execute(Boolean openedSettings) {
                AbstractActivity activity = activityReference.get();
                if (!openedSettings) {
                    notifyOnFineLocationDialogCancelled(activity);
                }
                removeObserver(activity);
                return null;
            }
        };
    }

    private void notifyOnFineLocationDialogCancelled(AbstractActivity activity) {
        Event event = new Event.Builder(R.id.onFineLocationDialogCancelled).build();
        activity.onUpdate(event);
    }

    @Override
    public long getActorAddress() {
        return R.id.addressFineLocationDisabledException;
    }

    @Override
    protected Executor<Message> createExecutor() {
        Executor<Message> executor = new Executor<>();
        executor.put((long) R.id.onPause, onPause());
        executor.put((long) R.id.onGPSStateChanged, onGpsStateChanged());
        return executor;
    }

    private Command<Message, ?> onGpsStateChanged() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                boolean gpsOn = message.getContent();
                if (gpsOn && dialog != null) {
                    dialog.dismiss();
                    removeObserver(activityReference.get());
                }
                return null;
            }
        };
    }

    private Command<Message, ?> onPause() {
        return new Command<Message, Void>() {
            @Override
            public Void execute(Message message) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                removeObserver(activityReference.get());
                return null;
            }
        };
    }
}
