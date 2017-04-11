package com.base.presentation.system;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.base.abstraction.commands.Command;
import com.base.abstraction.concurrency.ExecutionThread;
import com.base.abstraction.concurrency.Future;
import com.base.presentation.R;
import com.base.presentation.base.abstracts.features.AbstractActivity;

/**
 * a {@link Command} that shows the "enable GPS" dialog
 * <p>
 * Created by Ahmed Adel on 12/22/2016.
 */
public class FineLocationDialog implements
        Command<AbstractActivity, Future<FineLocationDialogAction>> {

    private Dialog dialog;

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public Future<FineLocationDialogAction> execute(AbstractActivity context) {

        Future<FineLocationDialogAction> future = new Future<>();
        future.onThread(ExecutionThread.MAIN);
        dialog = createAlertDialog(context, future);
        dialog.show();
        return future;
    }

    private Dialog createAlertDialog(Context context, final Future<FineLocationDialogAction> future) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.fine_location_dialog_title);
        builder.setMessage(R.string.fine_location_dialog_msg);
        builder.setPositiveButton(android.R.string.ok, onPositiveClick(future));
        builder.setNegativeButton(android.R.string.cancel, onNegativeClick(future));
        builder.setCancelable(false);
        return builder.create();
    }

    @NonNull
    private DialogInterface.OnClickListener onPositiveClick(
            final Future<FineLocationDialogAction> future) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                future.setResult(FineLocationDialogAction.POSITIVE_BUTTON);
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener onNegativeClick(
            final Future<FineLocationDialogAction> future) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                future.setResult(FineLocationDialogAction.NEGATIVE_BUTTON);
            }
        };
    }

}
