package com.base.presentation.views.dialogs;

import android.support.v7.app.AlertDialog;

import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * Created by Wafaa on 1/5/2017.
 */

public class CustomEventDialog extends EventDialog {

    protected CustomEventDialogBuilder dialogBuilder;

    public CustomEventDialog(CustomEventDialogBuilder customEventDialogBuilder,
                             AbstractActivity abstractActivity) {
        super(customEventDialogBuilder, abstractActivity);
        this.dialogBuilder = customEventDialogBuilder;
    }

    @Override
    public AlertDialog show() {
        AbstractActivity activity = abstractActivityRef.get();
        if (activity == null) {
            return null;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(dialogBuilder.getTitleText());
        alertDialogBuilder.setMessage(dialogBuilder.getMessageText());

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
}
