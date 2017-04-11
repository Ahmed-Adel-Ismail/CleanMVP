package com.base.presentation.views.dialogs;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.base.abstraction.commands.Command;
import com.base.abstraction.events.Event;
import com.base.abstraction.events.Message;
import com.base.abstraction.logs.Logger;
import com.base.presentation.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;

import java.lang.ref.WeakReference;

/**
 * A dialog that invokes {@link AbstractActivity#onUpdate(Event)} on it's interactions
 * <p/>
 * Created by Wafaa on 9/29/2016.
 */
public class EventDialog {

    private final EventDialogBuilder dialogBuilder;
    final WeakReference<AbstractActivity> abstractActivityRef;

    public EventDialog(EventDialogBuilder dialogBuilder, AbstractActivity abstractActivity) {
        this.dialogBuilder = dialogBuilder;
        this.abstractActivityRef = new WeakReference<>(abstractActivity);
    }


    public AlertDialog show() {
        AbstractActivity activity = abstractActivityRef.get();
        if (activity == null) {
            Logger.getInstance().error(EventDialog.class, "Activity destroyed before showing dialog");
            return null;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(dialogBuilder.getTitle());
        alertDialogBuilder.setCancelable(false);
        EventDialogLayout customLayout = dialogBuilder.getLayout();
        if (customLayout != null) {
            drawCustomLayout(activity, alertDialogBuilder, customLayout);
        } else {
            alertDialogBuilder.setMessage(dialogBuilder.getMessage());
        }

        if (dialogBuilder.hasPositiveTextResourceId()) {
            addPositiveButton(alertDialogBuilder);
        }

        if (dialogBuilder.hasNegativeTextResourceId()) {
            addNegativeButton(alertDialogBuilder);
        }

        if (dialogBuilder.hasNeutralTextResourceId()) {
            addNeutralButton(alertDialogBuilder);
        }
        return alertDialogBuilder.show();
    }

    private void drawCustomLayout(
            AbstractActivity activity,
            AlertDialog.Builder alertDialogBuilder,
            EventDialogLayout customLayout) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate((int) customLayout.getId(), null);
        alertDialogBuilder.setView(view);

        Command<EventDialogLayout.Params, Void> onInflate = customLayout.getOnInflate();
        if (onInflate != null) {
            EventDialogLayout.Params p = new EventDialogLayout.Params();
            p.dialogBuilder.set(dialogBuilder);
            p.activity.set(activity);
            p.dialogView.set(view);
            onInflate.execute(p);
        }
    }


    void addPositiveButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setPositiveButton(dialogBuilder.getPositiveText(),
                createPositiveOnClick());
    }

    @NonNull
    private DialogInterface.OnClickListener createPositiveOnClick() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AbstractActivity activity = abstractActivityRef.get();
                if (activity != null) {
                    updateActivity(activity, R.id.onDialogPositiveClick);
                }
            }
        };
    }

    void addNegativeButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(dialogBuilder.getNegativeText(),
                createNegativeOnClick());
    }

    @NonNull
    private DialogInterface.OnClickListener createNegativeOnClick() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AbstractActivity activity = abstractActivityRef.get();
                if (activity != null) {
                    updateActivity(activity, R.id.onDialogNegativeClick);
                }
            }
        };
    }


    void addNeutralButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNeutralButton(dialogBuilder.getNeutralText(),
                createNeutralOnClick());
    }

    @NonNull
    private DialogInterface.OnClickListener createNeutralOnClick() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AbstractActivity activity = abstractActivityRef.get();
                if (activity != null) {
                    updateActivity(activity, R.id.onDialogNeutralClick);
                }
            }
        };
    }

    private void updateActivity(AbstractActivity activity, int buttonId) {
        Message message = new Message.Builder().id(buttonId).content(dialogBuilder.getTag()).build();
        Event event = new Event.Builder(dialogBuilder.getDialogId())
                .message(message)
                .receiverActorAddresses(R.id.addressActivity)
                .build();
        activity.onUpdate(event);
        dialogBuilder.setTag(null);
    }

}
