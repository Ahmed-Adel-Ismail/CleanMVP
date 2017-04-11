package com.appzoneltd.lastmile.customer.features.pickup.recipient;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.appzoneltd.lastmile.customer.R;
import com.base.abstraction.commands.Command;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.listeners.OnTouchParams;

import java.lang.ref.WeakReference;

/**
 * Created by Wafaa on 1/6/2017.
 */

public class OnTouchCommandExecutor extends CommandExecutor<Long, OnTouchParams> {

    private WeakReference<AbstractActivity> contextRef;


    OnTouchCommandExecutor(AbstractActivity context) {
        contextRef = new WeakReference<>(context);
        Command<OnTouchParams, Void> command = createOnTouchCommand();
        put((long) R.id.shipping_service, command);
        put((long) R.id.shipment_service_type, command);
        put((long) R.id.country, command);
        put((long) R.id.city, command);
    }

    private Command<OnTouchParams, Void> createOnTouchCommand() {
        return new Command<OnTouchParams, Void>() {
            @Override
            public Void execute(OnTouchParams p) {
                Spinner spinner = (Spinner) p.getView();
                spinner.performClick();
                contextRef.get().getSystemServices().hideKeyboard();
                return null;
            }
        };
    }


}
