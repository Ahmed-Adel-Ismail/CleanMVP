package com.appzoneltd.lastmile.driver.features.pickup.payments;

import com.appzoneltd.lastmile.driver.R;
import com.appzoneltd.lastmile.driver.features.pickup.model.PickupProcessModel;
import com.base.abstraction.annotations.interfaces.Executable;
import com.base.abstraction.annotations.interfaces.Load;
import com.base.abstraction.events.Message;
import com.base.presentation.annotations.interfaces.OnClickHandler;
import com.base.presentation.base.presentation.Presenter;
import com.base.presentation.listeners.OnItemSelectedParam;
import com.base.usecases.annotations.ResponsesHandler;

@Load
@OnClickHandler(PaymentClickHandler.class)
@ResponsesHandler(PaymentResponsesHandler.class)
class PaymentPresenter extends Presenter<PaymentPresenter, PaymentViewModel, PickupProcessModel> {

    @Executable(R.id.onItemSelected)
    void onItemSelected(Message message) {
        if (message.getId() == R.id.dialog_pickup_process_cancel_reasons_spinner) {
            updateSelectedReasonIndex(message);
        }
    }

    private void updateSelectedReasonIndex(Message message) {
        OnItemSelectedParam p = message.getContent();
        CancellationDialogTag tag = (CancellationDialogTag) p.parent.getTag();
        tag.selectedReasonIndex.set(p.position);
    }


}
