package com.appzoneltd.lastmile.driver.features.pickup.payments;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.commands.Command;
import com.base.abstraction.exceptions.CheckedReferenceClearedException;
import com.base.abstraction.logs.Logger;
import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.base.presentation.PresenterClicksHandler;
import com.base.presentation.listeners.OnEventListener;
import com.base.presentation.listeners.TextWatcher;
import com.base.presentation.views.dialogs.EventDialog;
import com.base.presentation.views.dialogs.EventDialogBuilder;
import com.base.presentation.views.dialogs.EventDialogLayout;

class PaymentClickHandler extends
        PresenterClicksHandler<PaymentPresenter, PaymentViewModel, PickupProcessModel> {

    @Executable(R.id.screen_pickup_process_payment_buttons_submit)
    void onPaymentSubmitClick(View view) {
        getViewModel().progress.set(true);
    }

    @Executable(R.id.screen_pickup_process_payment_buttons_cancel)
    void onPaymentCancelClick(View view) {
        EventDialogLayout layout = new EventDialogLayout(R.layout.dialog_pickup_process_cancel);
        layout.onInflate(onInflateCancellationLayout());
        EventDialogBuilder builder = new EventDialogBuilder(R.id.onPickupProcessCancelPaymentDialog);
        builder.setTag(new CancellationDialogTag());
        builder.setTitle(R.string.screen_pickup_process_payment_cancellation_title);
        builder.setLayout(layout);
        builder.setPositiveText(R.string.screen_pickup_process_payment_cancellation_button_ok);
        builder.setNegativeText(R.string.screen_pickup_process_payment_cancellation_button_cancel);
        EventDialog dialog = new EventDialog(builder, getHostActivity());
        dialog.show();
    }

    @NonNull
    private Command<EventDialogLayout.Params, Void> onInflateCancellationLayout() {
        return new Command<EventDialogLayout.Params, Void>() {
            @Override
            public Void execute(EventDialogLayout.Params p) {
                try {
                    inflateCancellationLayout(p);
                } catch (CheckedReferenceClearedException e) {
                    Logger.getInstance().exception(e);
                }
                return null;
            }
        };
    }

    private void inflateCancellationLayout(EventDialogLayout.Params p) {
        View v = p.dialogView.get();
        AbstractActivity activity = p.activity.get();
        CancellationDialogTag tag = (CancellationDialogTag) p.dialogBuilder.get().getTag();
        createReasonsSpinner(tag, v).setAdapter(createReasonsAdapter(activity));
        TextInputEditText additionalNotesView = findAdditionalNotesView(v);
        additionalNotesView.addTextChangedListener(additionalNotesWatcher(tag));
    }

    @NonNull
    private TextWatcher additionalNotesWatcher(final CancellationDialogTag tag) {
        return new TextWatcher(0) {
            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    tag.additionalNotes.set(editable.toString());
                }
            }
        };
    }


    @NonNull
    private Spinner createReasonsSpinner(CancellationDialogTag tag, View v) {
        OnEventListener onEventListener = new OnEventListener(this);
        Spinner spinner = (Spinner) v.findViewById(R.id.dialog_pickup_process_cancel_reasons_spinner);
        spinner.setTag(tag);
        spinner.setOnTouchListener(onEventListener);
        spinner.setOnItemSelectedListener(onEventListener);
        return spinner;
    }

    @NonNull
    private ArrayAdapter<String> createReasonsAdapter(AbstractActivity activity) {
        String[] itemsArray = getModel().cancellationReasons.get().createTypesArray();
        int itemViewResource = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, itemViewResource, itemsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private TextInputEditText findAdditionalNotesView(View v) {
        return (TextInputEditText)
                v.findViewById(R.id.dialog_pickup_process_cancel_additional_notes_text_input_edit_text);
    }

}
